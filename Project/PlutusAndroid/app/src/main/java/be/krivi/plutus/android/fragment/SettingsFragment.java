package be.krivi.plutus.android.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.InputType;
import be.krivi.plutus.android.R;


/**
 * Created by Jan on 26/12/2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences( Bundle bundle, String s ){
        addPreferencesFromResource( R.xml.preference_panel );
    }

    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener( this );
        updateView();
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener( this );
        updateView();
    }

    @Override
    public void onSharedPreferenceChanged( SharedPreferences sharedPreferences, String key ){

        if( key.equals( "credit_rep_min" ) ){
            int min = Integer.parseInt( sharedPreferences.getString( key, "-1" ) );
            if( min < 0 || min > 100 ){
                EditTextPreference minimum = (EditTextPreference)findPreference( "credit_rep_min" );
                minimum.setText( "0" );
            }
        }else if( key.equals( "credit_rep_max" ) ){
            int min = Integer.parseInt( sharedPreferences.getString( "credit_rep_min", "-1" ) );
            int max = Integer.parseInt( sharedPreferences.getString( key, "-1" ) );
            if( max <= min || max < 0 || max > 100 ){
                EditTextPreference maximum = (EditTextPreference)findPreference( "credit_rep_max" );
                maximum.setText( "69" );
            }
        }
        updateView();
    }

    private void updateView(){
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

        EditTextPreference minimum = (EditTextPreference)findPreference( "credit_rep_min" );
        minimum.setSummary( "€ " + sp.getString( "credit_rep_min", "0" ) );

        EditTextPreference maximum = (EditTextPreference)findPreference( "credit_rep_max" );
        maximum.setSummary( "€ " + sp.getString( "credit_rep_max", "0" ) );
    }
}
