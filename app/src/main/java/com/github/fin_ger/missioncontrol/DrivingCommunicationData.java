package com.github.fin_ger.missioncontrol;

import com.github.fin_ger.missioncontrol.interfaces.ICommunicationData;

/**
 * Created by fin on 21.01.15.
 */
public
class DrivingCommunicationData implements ICommunicationData
{
    protected String data = "";

    @Override
    public
    String getSerializedData ()
    {
        return "#" + data + "%";
    }

    public void addActionAngle (int angle)
    {
        data += "{" + angle + "}";
    }

    public void addActionSpeedAngle (int speed, int angle)
    {
        data += "(" + speed + "," + angle + ")";
    }

    public void addActionDistanceAngle (int distance, int angle)
    {
        data += "[" + distance + "," + angle + "]";
    }
}
