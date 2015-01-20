package com.github.fin_ger.missioncontrol;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import it.neokree.materialnavigationdrawer.MaterialAccount;
import it.neokree.materialnavigationdrawer.MaterialAccountListener;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public
class ControlActivity extends MaterialNavigationDrawer implements MaterialAccountListener
{
    public LinkedList<Point> Points = new LinkedList<> ();
    protected NavigationPathProgrammerFragment pathProgrammer;

    @Override
    public
    void init (Bundle savedInstanceState)
    {
        // add first account
        MaterialAccount account = new MaterialAccount ("Mission Control", "Arduino ATmega 2560", this.getResources ().getDrawable (
            R.drawable.mission_control_flat), this.getResources ().getDrawable (R.drawable.arduino_bg));

        pathProgrammer = new NavigationPathProgrammerFragment ();

        this.addAccount (account);

        // set listener
        this.setAccountListener (this);

        this.addSection (this.newSection (this.getString (R.string.console), new ConsoleFragment ()));
        this.addSection (this.newSection (this.getString (R.string.connection),
                                          new ConnectionFragment ()).setNotificationsText ("dis"));
        this.addSubheader (this.getString (R.string.navigation));
        this.addSection (this.newSection (this.getString (R.string.path_programmer), R.drawable.ic_path_programmer,
                                          pathProgrammer));
        this.addSection (this.newSection (this.getString (R.string.trackpad), R.drawable.ic_trackpad_navigator,
                                          pathProgrammer));
        this.addDivisor ();
        this.addBottomSection (this.newSection (this.getString (R.string.about), R.drawable.ic_about,
                                                new PlaceholderFragment ()));

        this.setBackPattern (MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);
    }

    public void onResetClicked (View view)
    {
        pathProgrammer.reset ();
        Points.clear ();
    }

    public void onSubmitClicked (View view)
    {}

    @Override
    public
    void onAccountOpening (MaterialAccount account)
    {
    }

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
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    @Override
    public
    void onChangeAccount (MaterialAccount newAccount)
    {
        // when another account is selected
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static
    class PlaceholderFragment extends Fragment
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public
        PlaceholderFragment ()
        {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static
        PlaceholderFragment newInstance (int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment ();
            Bundle args = new Bundle ();
            args.putInt (ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments (args);
            return fragment;
        }

        @Override
        public
        View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            return inflater.inflate (R.layout.fragment_control, container, false);
        }
    }
}
