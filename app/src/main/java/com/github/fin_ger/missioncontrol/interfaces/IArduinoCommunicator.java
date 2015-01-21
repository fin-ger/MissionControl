package com.github.fin_ger.missioncontrol.interfaces;

import com.github.fin_ger.missioncontrol.events.OnConnectionStateChanged;
import com.github.fin_ger.missioncontrol.events.OnDataReceived;

/**
 * Created by fin on 21.01.15.
 */
public
interface IArduinoCommunicator
{
    void setOnConnectionChangedListener (OnConnectionStateChanged listener);
    void setOnDataReceivedListener (OnDataReceived listener);
    void writeRaw (String text);
    void write (ICommunicationData data);
    void enableCommunication ();
    void disableCommunication ();
    boolean init ();
}
