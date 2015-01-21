package com.github.fin_ger.missioncontrol;

import com.github.fin_ger.missioncontrol.interfaces.ICommunicationData;

/**
 * Created by fin on 21.01.15.
 */
public
class DriveCommunicationData implements ICommunicationData
{
    protected String data = "";

    @Override
    public
    String getSerializedData ()
    {
        return "#" + data + "%";
    }

    public void addActionAngle (short angle)
    {
        data += "{" + angle + "}";
    }

    public void addActionSpeedAngle (short speed, short angle)
    {
        data += "(" + speed + "," + angle + ")";
    }

    public void addActionDistanceAngle (short distance, short angle)
    {
        data += "[" + distance + "," + angle + "]";
    }
}
