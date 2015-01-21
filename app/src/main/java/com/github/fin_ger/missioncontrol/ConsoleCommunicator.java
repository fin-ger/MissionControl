package com.github.fin_ger.missioncontrol;

import android.os.Handler;

import com.github.fin_ger.missioncontrol.events.OnConnectionStateChanged;
import com.github.fin_ger.missioncontrol.events.OnDataReceived;
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
        run = false;
    }

    @Override
    public
    void setOnConnectionChangedListener (OnConnectionStateChanged listener)
    {
        listener.onConnectionStateChanged (true);
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
                {
                    e.printStackTrace ();
                }

                if (inputData != null && inputDataListener != null)
                    inputDataListener.onDataReceived (inputData);

                inputData = null;

                if (run)
                    handler.postDelayed (this, 250);
            }
        };

        run ();

        return true;
    }

    protected void run ()
    {
        handler.post (runnable);
    }

    protected boolean        run;
    protected BufferedReader stdin;
    protected String         inputData;
    protected Handler        handler;
    protected Runnable       runnable;
    protected OnDataReceived inputDataListener;
}
