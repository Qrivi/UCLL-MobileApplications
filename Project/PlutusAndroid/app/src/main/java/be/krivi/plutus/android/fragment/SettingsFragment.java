package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.dialog.BaseDialog;
import be.krivi.plutus.android.dialog.EditTextDialog;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Jan on 27/12/2015.
 */
public class SettingsFragment extends Fragment implements EditTextDialog.NoticeDialogListener{


    @Bind( R.id.pref_switchCreditGauge )
    SwitchCompat switchCreditGauge;

    @Bind( R.id.pref_switchNotifications )
    SwitchCompat switchNotifications;

    @Bind( R.id.pef_wrapperCredit_gaugeMin )
    LinearLayout wrapperGaugeMin;

    @Bind( R.id.pef_wrapperCredit_gaugeMax )
    LinearLayout wrapperGaugeMax;

    @Bind( R.id.pref_hintCreditGaugeMin )
    TextView hintGaugeMin;

    @Bind( R.id.pref_hintCreditGaugeMax )
    TextView hintGaugeMax;

    @Bind( R.id.pref_hintLanguage )
    TextView hintLanguage;


    private PlutusAndroid app;
    private MainActivity main;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

        main = (MainActivity)getActivity();
        app = (PlutusAndroid)main.getApplication();

        updateView();
        return view;
    }

    private void updateView(){
        switchCreditGauge.setChecked( app.getCreditRepresentation() );
        hintGaugeMin.setText( "€ " + app.getCreditRepresentationMin() );
        hintGaugeMax.setText( "€ " + app.getCreditRepresentationMax() );
        //hintLanguage.setText(  );
    }

    @OnClick( R.id.pref_wrapperCredit_gaugeSwitch )
    public void onGaugeSwitchWrapperClicked(){
        switchCreditGauge.setChecked( !switchCreditGauge.isChecked() );
        onGaugeSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_switchCreditGauge )
    public void onGaugeSwitchChanged(){
        app.setCreditRepresentation( switchCreditGauge.isChecked() );
        if( switchCreditGauge.isChecked() ){
            wrapperGaugeMin.setClickable( true );
            wrapperGaugeMax.setClickable( true );
        }else{
            wrapperGaugeMin.setClickable( false );
            wrapperGaugeMax.setClickable( false );
        }
    }

    @OnClick( R.id.pref_wrapperNotifications_notificationSwitch )
    public void onNotificationSwitchClicked(){
        Message.toast( app.getApplicationContext(), "Not yet present in this beta!" );
    }

    @OnCheckedChanged( R.id.pref_switchNotifications )
    public void onNotificationsSwitchChanged(){
        switchNotifications.setChecked( false );
        Message.toast( app.getApplicationContext(), getString( R.string.not_in_beta) );
    }

    @OnClick( R.id.pef_wrapperCredit_gaugeMin )
    public void onWrapperGaugeMinClicked(){
        createEditTextDialog( getString( R.string.minimum ) );
    }

    @OnClick( R.id.pef_wrapperCredit_gaugeMax )
    public void onWrapperGaugeMaxClicked(){
        createEditTextDialog( getString( R.string.maximum ) );
    }

    @OnClick( R.id.pref_wrapperNotifications_language )
    public void onLanguageWrapperClicked(){
        Log.v( "rip", "language" );
    }

    @Override
    public void onDialogPositiveClick( BaseDialog dialog ){
        if( dialog.getType().equals( getString( R.string.minimum ) ) ){
            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
            app.setCreditRepresentationMin( Integer.parseInt( edit.getText().toString() )) ;
        }else if( dialog.getType().equals( getString( R.string.maximum ) ) ){
            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
            app.setCreditRepresentationMax( Integer.parseInt( edit.getText().toString() ) );
        }
        updateView();
    }

    @Override
    public void onDialogNegativeClick( BaseDialog dialog ){
            dialog.getDialog().cancel();
    }

    private void createEditTextDialog( String type ){
        BaseDialog dialog = new EditTextDialog();
        dialog.setTargetFragment( this, 1 );
        dialog.setDialogEditInfo( type );
        dialog.show( getFragmentManager(), type );
    }
}
