package com.github.fin_ger.missioncontrol;

import com.github.fin_ger.missioncontrol.events.IConnectionStateListener;
import com.github.fin_ger.missioncontrol.events.IInputDataListener;
import com.github.fin_ger.missioncontrol.interfaces.IArduinoCommunicator;
import com.github.fin_ger.missioncontrol.interfaces.ICommunicationData;

/**
 * Created by fin on 21.01.15.
 */
public
class TelnetArduinoCommunicator implements IArduinoCommunicator
{
    @Override
    public
    void setOnConnectionChangedListener (IConnectionStateListener listener)
    {

    }

    @Override
    public
    void setOnDataReceivedListener (IInputDataListener listener)
    {

    }

    @Override
    public
    void writeRaw (String text)
    {

    }

    @Override
    public
    void write (ICommunicationData data)
    {
        writeRaw (data.getSerializedData ());
    }
}
