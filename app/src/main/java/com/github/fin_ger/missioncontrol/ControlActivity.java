package com.github.fin_ger.missioncontrol;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.fin_ger.missioncontrol.fragments.AboutFragment;
import com.github.fin_ger.missioncontrol.fragments.ConnectionFragment;
import com.github.fin_ger.missioncontrol.fragments.ConsoleFragment;
import com.github.fin_ger.missioncontrol.fragments.NavigationPathProgrammerFragment;
import com.github.fin_ger.missioncontrol.fragments.TrackpadFragment;

import java.util.LinkedList;

import it.neokree.materialnavigationdrawer.MaterialAccount;
import it.neokree.materialnavigationdrawer.MaterialAccountListener;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public
class ControlActivity extends MaterialNavigationDrawer implements MaterialAccountListener
{
    public LinkedList<Point> Points = new LinkedList<> ();
    protected NavigationPathProgrammerFragment pathProgrammer;
    protected ConnectionFragment               connection;

    @Override
    public
    void init (Bundle savedInstanceState)
    {
        // add first account
        MaterialAccount account = new MaterialAccount ("Mission Control", "Arduino ATmega 2560", this.getResources ()
                                                                                                     .getDrawable (
                                                                                                         R.drawable.mission_control_flat),
                                                       this.getResources ().getDrawable (R.drawable.arduino_bg));

        pathProgrammer = new NavigationPathProgrammerFragment ();

        this.addAccount (account);

        this.addSection (this.newSection (this.getString (R.string.console), new ConsoleFragment ()));

        connection = new ConnectionFragment ();
        connection.section = this.newSection (this.getString (R.string.connection), connection);
        this.addSection (connection.section);
        connection.disconnected ();

        this.addSubheader (this.getString (R.string.navigation));
        this.addSection (this.newSection (this.getString (R.string.path_programmer), R.drawable.ic_path_programmer,
                                          pathProgrammer));
        this.addSection (this.newSection (this.getString (R.string.trackpad), R.drawable.ic_trackpad_navigator,
                                          new TrackpadFragment ()));
        this.addDivisor ();
        this.addBottomSection (this.newSection (this.getString (R.string.about), R.drawable.ic_about,
                                                new AboutFragment ()));

        this.allowArrowAnimation ();
        this.setBackPattern (MaterialNavigationDrawer.BACKPATTERN_BACK_ANYWHERE);
    }

    public void onResetClicked (View view)
    {
        pathProgrammer.reset ();
        Points.clear ();
    }

    public void onSubmitClicked (View view)
    {
        //TODO: parse Points to distance and angle
    }

    protected void showSettings ()
    {
        Toast.makeText (getApplicationContext (), getString (R.string.not_implemented_yet), Toast.LENGTH_LONG).show ();
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
