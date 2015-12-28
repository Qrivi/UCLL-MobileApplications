package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.application.PlutusAndroid;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by Jan on 27/12/2015.
 */
public class SettingsFragment extends Fragment{


    @Bind( R.id.pref_switchCreditGauge )
    Switch switchCreditGauge;

    private PlutusAndroid app;
    private MainActivity main;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

        main = (MainActivity) getActivity();
        app = (PlutusAndroid) main.getApplication();

        return view;
    }



    @OnClick(R.id.pref_wrapperCredit_gaugeSwitch)
    public void onGaugeSwitchClicked() {

        switchCreditGauge.setEnabled( true );
    }






}
