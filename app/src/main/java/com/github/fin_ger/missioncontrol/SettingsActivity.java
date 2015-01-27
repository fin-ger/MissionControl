package com.github.fin_ger.missioncontrol;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public
class SettingsActivity extends PreferenceActivity
{
    @Override
    protected
    void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById (android.R.id.list).getParent ().getParent ().getParent ();
        Toolbar bar = (Toolbar) LayoutInflater.from (this).inflate (R.layout.settings_toolbar, root, false);
        root.addView (bar, 0);
        bar.setNavigationOnClickListener (new View.OnClickListener ()
        {
            @Override
            public
            void onClick (View v)
            {
                finish ();
            }
        });
    }

    @Override
    protected
    void onPostCreate (Bundle savedInstanceState)
    {
        super.onPostCreate (savedInstanceState);

        // Add 'general' preferences.
        addPreferencesFromResource (R.xml.pref_general);

        // Add 'notifications' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory (this);
        fakeHeader.setTitle (R.string.pref_header_connection);
        getPreferenceScreen ().addPreference (fakeHeader);
        addPreferencesFromResource (R.xml.pref_connection);
    }


}
