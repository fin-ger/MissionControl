package com.github.fin_ger.missioncontrol;

import android.os.AsyncTask;
import android.os.Handler;

import com.github.fin_ger.missioncontrol.events.OnConnectionStateChanged;
import com.github.fin_ger.missioncontrol.events.OnDataReceived;
import com.github.fin_ger.missioncontrol.events.OnStatusMessage;
import com.github.fin_ger.missioncontrol.interfaces.IArduinoCommunicator;
import com.github.fin_ger.missioncontrol.interfaces.ICommunicationData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by fin on 21.01.15.
 */
public
class TCPCommunicator implements IArduinoCommunicator
{
    public
    TCPCommunicator ()
    {
        super ();
        run = false;
        lastConnectionState = false;
        statusMsg = "";
    }

    @Override
    public
    void setOnConnectionChangedListener (OnConnectionStateChanged listener)
    {
        connectionStateListener = listener;
    }

    @Override
    public
    void setOnDataReceivedListener (OnDataReceived listener)
    {
        dataListener = listener;
    }

    @Override
    public
    void setOnStatusMessageListener (OnStatusMessage listener)
    {
        statusMessageListener = listener;
    }

    @Override
    public
    void writeRaw (String text)
    {
        if (tcpWriter != null && !tcpWriter.checkError ())
        {
            tcpWriter.println (text);
            tcpWriter.flush ();
        }
    }

    @Override
    public
    void write (ICommunicationData data)
    {
        writeRaw (data.getSerializedData ());
    }

    @Override
    public
    void enableCommunication ()
    {
        run = true;

        asyncTask = new AsyncTask<Void, Void, Void> ()
        {
            @Override
            protected
            Void doInBackground (Void... params)
            {
                try
                {
                    InetAddress serverAddr = InetAddress.getByName (serverIP);

                    socket = new Socket (serverAddr, serverPort);
                }
                catch (Exception e)
                {
                    statusMsg = e.getLocalizedMessage ();
                    initStatus = false;
                    return null;
                }

                initStatus = true;
                return null;
            }

            @Override
            protected
            void onPostExecute (Void param)
            {
                if (!initStatus)
                {
                    statusMessageListener.onStatusMessage (statusMsg, initStatus);
                    return;
                }

                try
                {
                    tcpWriter = new PrintWriter (new BufferedWriter (new OutputStreamWriter (socket.getOutputStream ())),
                                                 true);
                    tcpReader = new BufferedReader (new InputStreamReader (socket.getInputStream ()));

                    handler = new Handler ();

                    runnable = new Runnable ()
                    {
                        @Override
                        public
                        void run ()
                        {
                            new AsyncTask<Void, Void, Void> ()
                            {
                                String data;

                                @Override
                                protected
                                Void doInBackground (Void... params)
                                {
                                    try
                                    {
                                        while (tcpReader.ready ())
                                        {
                                            data = tcpReader.readLine ();

                                            if (serverData == null)
                                                serverData = data + "\n";
                                            else
                                                serverData += data + "\n";
                                        }
                                    }
                                    catch (Exception e)
                                    {}

                                    return null;
                                }

                                @Override
                                protected
                                void onPostExecute (Void param)
                                {
                                    if (serverData != null && dataListener != null)
                                        dataListener.onDataReceived (serverData);

                                    serverData = null;

                                    if (run)
                                        handler.postDelayed (runnable, 250);
                                    else
                                        try
                                        {
                                            socket.close ();
                                        }
                                        catch (Exception e)
                                        {}
                                }
                            }.execute ();
                        }
                    };

                    run ();
                }
                catch (Exception e)
                {
                    initStatus = false;
                    statusMsg = e.getLocalizedMessage ();
                    return;
                }

                connectionHandler = new Handler ();
                connectionRunnable = new Runnable ()
                {
                    @Override
                    public
                    void run ()
                    {
                        boolean connection = socket.isConnected () && !socket.isClosed ();
                        if (connection != lastConnectionState)
                        {
                            connectionStateListener.onConnectionStateChanged (connection);
                            lastConnectionState = connection;
                        }
                        if (!socket.isClosed ())
                            connectionHandler.postDelayed (connectionRunnable, 5000);
                    }
                };

                connectionHandler.post (connectionRunnable);

                if (!initStatus)
                    statusMessageListener.onStatusMessage (statusMsg, initStatus);
            }
        };

        asyncTask.execute ();
    }

    @Override
    public
    void disableCommunication ()
    {
        run = false;
    }

    public void setServerIP (String ip)
    {
        this.serverIP = ip;
    }

    public String getServerIP ()
    {
        return this.serverIP;
    }

    public void setServerPort (int port)
    {
        this.serverPort = port;
    }

    public int getServerPort ()
    {
        return this.serverPort;
    }

    protected void run ()
    {
        handler.post (runnable);
    }

    protected OnConnectionStateChanged    connectionStateListener;
    protected OnDataReceived              dataListener;
    protected OnStatusMessage             statusMessageListener;
    protected Socket                      socket;
    protected PrintWriter                 tcpWriter;
    protected BufferedReader              tcpReader;
    protected boolean                     run;
    protected String                      serverData;
    protected Runnable                    runnable;
    protected Handler                     handler;
    protected Runnable                    connectionRunnable;
    protected Handler                     connectionHandler;
    protected boolean                     lastConnectionState;
    protected String                      statusMsg;
    protected boolean                     initStatus;
    protected AsyncTask<Void, Void, Void> asyncTask;

    public String serverIP;
    public int    serverPort;
}
