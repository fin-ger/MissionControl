package com.github.fin_ger.missioncontrol;

import android.os.Handler;

import com.github.fin_ger.missioncontrol.events.OnConnectionStateChanged;
import com.github.fin_ger.missioncontrol.events.OnDataReceived;
import com.github.fin_ger.missioncontrol.events.OnStatusMessage;
import com.github.fin_ger.missioncontrol.interfaces.IArduinoCommunicator;
import com.github.fin_ger.missioncontrol.interfaces.ICommunicationData;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by fin on 21.01.15.
 */
public
class ConsoleCommunicator implements IArduinoCommunicator
{
    public
    ConsoleCommunicator ()
    {
        super ();
        run = false;
    }

    @Override
    public
    void setOnConnectionChangedListener (OnConnectionStateChanged listener)
    {
        connectionStateChangedListener = listener;
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
        System.out.println (text);
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
        init ();
        connectionStateChangedListener.onConnectionStateChanged (true);
    }

    @Override
    public
    void disableCommunication ()
    {
        run = false;
        connectionStateChangedListener.onConnectionStateChanged (false);
    }

    protected
    void init ()
    {
        stdin = new BufferedReader (new InputStreamReader (System.in));

        handler = new Handler ();

        runnable = new Runnable ()
        {
            @Override
            public
            void run ()
            {
                try
                {
                    inputData = stdin.readLine ();
                }
                catch (Exception e)
                {}

                if (inputData != null && dataListener != null)
                    dataListener.onDataReceived (inputData);

                inputData = null;

                if (run)
                    handler.postDelayed (this, 250);
            }
        };

        run ();
    }

    protected void run ()
    {
        handler.post (runnable);
    }

    protected boolean                  run;
    protected BufferedReader           stdin;
    protected String                   inputData;
    protected Handler                  handler;
    protected Runnable                 runnable;
    protected OnDataReceived           dataListener;
    protected OnConnectionStateChanged connectionStateChangedListener;
    protected OnStatusMessage          statusMessageListener;
}
