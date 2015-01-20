package com.github.fin_ger.missioncontrol;

import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.neokree.materialnavigationdrawer.MaterialSection;

public
class ConnectionFragment extends Fragment
{
    public
    ConnectionFragment ()
    {
        // Required empty public constructor
    }

    public MaterialSection section;

    @Override
    public
    void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
    }

    @Override
    public
    View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate (R.layout.fragment_connection, container, false);
        Typeface monotypeface = Typeface.createFromAsset (getActivity ().getApplicationContext ().getAssets (),
                                                          "fonts/DejaVuSansMono.ttf");
        TextView tv = (TextView) v.findViewById (R.id.ping_console);
        tv.setTypeface (monotypeface);
        return v;
    }

    public void connected ()
    {
        if (section != null)
            section.setNotificationsText ("\u2713");
    }

    public void disconnected ()
    {
        if (section != null)
            section.setNotificationsText ("\u2717");
    }

    @Override
    public
    void onAttach (Activity activity)
    {
        super.onAttach (activity);
    }

    @Override
    public
    void onDetach ()
    {
        super.onDetach ();
    }
}
