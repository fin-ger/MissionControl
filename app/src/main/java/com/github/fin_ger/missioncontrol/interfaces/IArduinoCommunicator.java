package com.github.fin_ger.missioncontrol.interfaces;

import com.github.fin_ger.missioncontrol.events.IConnectionStateListener;
import com.github.fin_ger.missioncontrol.events.IInputDataListener;

/**
 * Created by fin on 21.01.15.
 */
public
interface IArduinoCommunicator
{
    void setOnConnectionChangedListener (IConnectionStateListener listener);
    void setOnDataReceivedListener (IInputDataListener listener);
    void writeRaw (String text);
    void write (ICommunicationData data);
}
