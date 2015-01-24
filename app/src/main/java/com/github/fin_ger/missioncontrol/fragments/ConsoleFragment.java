package com.github.fin_ger.missioncontrol.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
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

        final ScrollView scroll = (ScrollView) (v.findViewById (R.id.console_scroll));

        tv.addTextChangedListener (new TextWatcher ()
        {
            @Override
            public
            void beforeTextChanged (CharSequence s, int start, int count, int after)
            {}

            @Override
            public
            void onTextChanged (CharSequence s, int start, int before, int count)
            {}

            @Override
            public
            void afterTextChanged (Editable s)
            {
                scroll.fullScroll (ScrollView.FOCUS_DOWN);
            }
        });

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
