package com.github.fin_ger.missioncontrol;

import android.os.Handler;

import com.github.fin_ger.missioncontrol.events.OnConnectionStateChanged;
import com.github.fin_ger.missioncontrol.events.OnDataReceived;
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
        serverIP = "172.16.225.19";//"10.100.0.1";
        serverPort = 2000;
        run = false;
        lastConnectionState = false;
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
        inputDataListener = listener;
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
        run ();
    }

    @Override
    public
    void disableCommunication ()
    {
        run = false;
    }

    @Override
    public
    boolean init ()
    {
        try
        {
            InetAddress serverAddr = InetAddress.getByName (this.serverIP);

            final Socket socket = new Socket (serverAddr, serverPort);

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
                        try
                        {
                            serverData = tcpReader.readLine ();
                        }
                        catch (Exception e)
                        {}

                        if (serverData != null && inputDataListener != null)
                            inputDataListener.onDataReceived (serverData);

                        serverData = null;

                        if (run)
                            handler.postDelayed (this, 250);
                        else
                            try
                            {
                                socket.close ();
                            }
                            catch (Exception e)
                            {}
                    }
                };

                run ();
            }
            catch (Exception e)
            {
                return false;
            }

            connectionHandler = new Handler ();
            connectionRunnable = new Runnable ()
            {
                @Override
                public
                void run ()
                {
                    boolean connection = socket.isConnected ();
                    if (connection != lastConnectionState)
                    {
                        connectionStateListener.onConnectionStateChanged (connection);
                        lastConnectionState = connection;
                    }
                    connectionHandler.postDelayed (connectionRunnable, 5000);
                }
            };

            connectionHandler.post (connectionRunnable);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
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

    protected OnConnectionStateChanged connectionStateListener;
    protected OnDataReceived           inputDataListener;
    protected String                   serverIP;
    protected int                      serverPort;
    protected PrintWriter              tcpWriter;
    protected BufferedReader           tcpReader;
    protected boolean                  run;
    protected String                   serverData;
    protected Runnable                 runnable;
    protected Handler                  handler;
    protected Runnable                 connectionRunnable;
    protected Handler                  connectionHandler;
    protected boolean                  lastConnectionState;
}
