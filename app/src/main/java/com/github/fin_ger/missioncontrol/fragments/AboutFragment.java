package com.github.fin_ger.missioncontrol.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.fin_ger.missioncontrol.R;

public
class AboutFragment extends Fragment
{
    public
    AboutFragment ()
    {
        // Required empty public constructor
    }

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
        return inflater.inflate (R.layout.fragment_about, container, false);
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
