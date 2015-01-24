package com.github.fin_ger.missioncontrol;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.fin_ger.missioncontrol.events.OnConnectionStateChanged;
import com.github.fin_ger.missioncontrol.events.OnDataReceived;
import com.github.fin_ger.missioncontrol.events.OnReset;
import com.github.fin_ger.missioncontrol.events.OnStatusMessage;
import com.github.fin_ger.missioncontrol.fragments.AboutFragment;
import com.github.fin_ger.missioncontrol.fragments.ConsoleFragment;
import com.github.fin_ger.missioncontrol.fragments.NavigationPathProgrammerFragment;
import com.github.fin_ger.missioncontrol.fragments.TrackpadFragment;
import com.github.fin_ger.missioncontrol.interfaces.IArduinoCommunicator;

import java.util.LinkedList;

import it.neokree.materialnavigationdrawer.MaterialAccount;
import it.neokree.materialnavigationdrawer.MaterialAccountListener;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.MaterialSection;
import it.neokree.materialnavigationdrawer.MaterialSectionListener;

public
class ControlActivity extends MaterialNavigationDrawer implements MaterialAccountListener
{
    public LinkedList<Point> Points = new LinkedList<> ();
    protected NavigationPathProgrammerFragment pathProgrammer;
    protected MaterialSection                  connection;
    protected boolean                          connectionState;
    protected IArduinoCommunicator             communicator;
    protected String                           consoleText;

    @Override
    public
    void init (Bundle savedInstanceState)
    {
        consoleText = "";
        communicator = new TCPCommunicator ();
        //communicator = new ConsoleCommunicator ();

        // add first account
        MaterialAccount account =
            new MaterialAccount ("Mission Control", "Arduino ATmega 2560",
                                 this.getResources ().getDrawable (R.drawable.mission_control_flat),
                                 this.getResources ().getDrawable (R.drawable.arduino_bg));

        pathProgrammer = new NavigationPathProgrammerFragment ();
        pathProgrammer.setOnResetListener (new OnReset ()
        {
            @Override
            public
            void onReset ()
            {
                Points.clear ();
            }
        });

        this.addAccount (account);

        this.addSection (this.newSection (this.getString (R.string.console), new ConsoleFragment ()));

        connection = this.newSection (this.getString (R.string.connection), new MaterialSectionListener ()
        {
            @Override
            public
            void onClick (MaterialSection materialSection)
            {
                if (connectionState)
                {
                    Toast.makeText (getApplicationContext (), getString (R.string.closing_connection),
                                    Toast.LENGTH_SHORT).show ();
                    communicator.disableCommunication ();
                }
                else
                {
                    Toast.makeText (getApplicationContext (), getString (R.string.connecting), Toast.LENGTH_SHORT)
                         .show ();
                    communicator.enableCommunication ();
                }
            }
        });
        this.addSection (connection);

        this.addSubheader (this.getString (R.string.navigation));

        this.addSection (
            this.newSection (this.getString (R.string.path_programmer), R.drawable.ic_path_programmer, pathProgrammer)
                .setSectionColor (getResources ().getColor (R.color.mission_control),
                                  getResources ().getColor (R.color.mission_control_dark)));

        this.addSection (
            this.newSection (this.getString (R.string.trackpad), R.drawable.ic_trackpad_navigator,
                             new TrackpadFragment ())
                .setSectionColor (getResources ().getColor (R.color.mission_control),
                                  getResources ().getColor (R.color.mission_control_dark)));

        this.addBottomSection (
            this.newSection (this.getString (R.string.about), R.drawable.ic_about, new AboutFragment ())
                .setSectionColor (getResources ().getColor (R.color.mission_control),
                                  getResources ().getColor (R.color.mission_control_dark)));

        this.allowArrowAnimation ();
        this.setBackPattern (MaterialNavigationDrawer.BACKPATTERN_BACK_ANYWHERE);

        connection.setNotificationsText ("\u2717");

        communicator.setOnConnectionChangedListener (new OnConnectionStateChanged ()
        {
            @Override
            public
            void onConnectionStateChanged (boolean state)
            {
                if (state)
                {
                    connection.setNotificationsText ("\u2713");
                    Toast.makeText (getApplicationContext (), getString (R.string.connected), Toast.LENGTH_SHORT)
                         .show ();
                }
                else
                {
                    connection.setNotificationsText ("\u2717");
                    Toast.makeText (getApplicationContext (), getString (R.string.disconnected), Toast.LENGTH_SHORT)
                         .show ();
                }
                connectionState = state;
            }
        });

        communicator.setOnDataReceivedListener (new OnDataReceived ()
        {
            @Override
            public
            void onDataReceived (String data)
            {
                consoleText += data;
                TextView tv = (TextView) findViewById (R.id.console);

                if (tv == null)
                    return;

                tv.append (data);
            }
        });

        communicator.setOnStatusMessageListener (new OnStatusMessage ()
        {
            @Override
            public
            void onStatusMessage (String statusMessage, boolean status)
            {
                if (!status)
                    Toast.makeText (getApplicationContext (), getString (R.string.connection_failed) + " " +
                                    statusMessage, Toast.LENGTH_LONG).show ();
            }
        });

        communicator.enableCommunication ();
    }

    public void onResetClicked (View view)
    {
        pathProgrammer.reset ();
    }

    public void onSubmitClicked (View view)
    {
        if (Points.size () <= 0)
        {
            Toast.makeText (getApplicationContext (), getString (R.string.no_path), Toast.LENGTH_LONG).show ();
            return;
        }

        DriveCommunicationData data = new DriveCommunicationData ();
        float aa = Points.get (0).x;
        float bb = Points.get (0).y;
        float oa = 0;
        float ob = -1;
        float a,b,x,y;
        short d, p;

        for (int i = 1; i < Points.size (); i++)
        {
            x = Points.get (i).x;
            y = Points.get (i).y;
            a = x - aa;
            b = y - bb;

            d = (short) Math.sqrt (a * a + b * b);
            p = (short) (Math.toDegrees (Math.atan2 (b, a) - Math.atan2 (ob, oa)));

            aa = x;
            bb = y;
            oa = a;
            ob = b;

            while (p < -180)
                p += 360;

            while (p > 180)
                p -= 360;

            p += 180;

            data.addActionAngle (p);
            data.addActionDistanceAngle (d, (short) 0);
        }

        communicator.write (data);
    }

    protected void showSettings ()
    {
        Toast.makeText (getApplicationContext (), getString (R.string.not_implemented_yet), Toast.LENGTH_LONG).show ();
    }

    public String getConsoleText ()
    {
        return consoleText;
    }

    @Override
    public
    void onAccountOpening (MaterialAccount account)
    {}

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate (R.menu.global, menu);
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId ())
        {
            case R.id.action_settings:
                showSettings ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    @Override
    public
    void onChangeAccount (MaterialAccount newAccount)
    {}
}
