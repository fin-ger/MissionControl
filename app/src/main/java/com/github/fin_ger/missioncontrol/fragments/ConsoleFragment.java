package com.github.fin_ger.missioncontrol.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.fin_ger.missioncontrol.ControlActivity;
import com.github.fin_ger.missioncontrol.R;

import java.util.ResourceBundle;

public
class ConsoleFragment extends Fragment
{
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
        View v = inflater.inflate (R.layout.fragment_console, container, false);
        Typeface monotypeface = Typeface.createFromAsset (getActivity ().getApplicationContext ().getAssets (),
                                                          "fonts/DejaVuSansMono.ttf");
        TextView tv = (TextView) v.findViewById (R.id.console);
        tv.setTypeface (monotypeface);

        if (getActivity () instanceof ControlActivity)
            tv.append (((ControlActivity) getActivity ()).getConsoleText ());

        return v;
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
