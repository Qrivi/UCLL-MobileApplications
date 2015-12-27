package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import be.krivi.plutus.android.R;


/**
 * Created by Jan on 26/12/2015.
 */
public class SettingsFragment extends PreferenceFragmentCompat{

    @Override
    public void onCreatePreferences( Bundle bundle, String s ){
        addPreferencesFromResource( R.xml.panel_preferences );
    }
}
