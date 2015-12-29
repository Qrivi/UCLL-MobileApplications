package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.application.Language;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.dialog.BaseDialog;
import be.krivi.plutus.android.dialog.ConfirmationDialog;
import be.krivi.plutus.android.dialog.EditTextDialog;
import be.krivi.plutus.android.dialog.ListDialog;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import java.util.Arrays;
import java.util.List;

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

    @Bind( R.id.pref_hintHomescreen )
    TextView hintHomescreen;

    private PlutusAndroid app;
    private MainActivity main;

    private List<String> languages;
    private List<String> screens;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

        main = (MainActivity)getActivity();
        app = (PlutusAndroid)main.getApplication();

        languages = Arrays.asList( getResources().getStringArray( R.array.languages ) );
        screens = Arrays.asList( getResources().getStringArray( R.array.screens ) );

        updateView();
        return view;
    }

    private void updateView(){
        switchCreditGauge.setChecked( app.getCreditRepresentation() );
        hintGaugeMin.setText( "€ " + app.getCreditRepresentationMin() );
        hintGaugeMax.setText( "€ " + app.getCreditRepresentationMax() );
        hintLanguage.setText( app.getLanguage().toString() );
        //hintHomescreen.setText( app.getHomeScreen() );
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
        Message.toast( app.getApplicationContext(), getString( R.string.not_in_beta ) );
    }

    @OnCheckedChanged( R.id.pref_switchNotifications )
    public void onNotificationsSwitchChanged(){
        switchNotifications.setChecked( false );
        Message.toast( app.getApplicationContext(), getString( R.string.not_in_beta ) );
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
        createListDialog( getString( R.string.language ), languages.indexOf( app.getLanguage().toString() ), (String[])languages.toArray() );
    }

    @OnClick( R.id.pref_wrapperHomeScreen )
    public void onHomeScreenClicked(){
        createListDialog( getString( R.string.home_screen ), screens.indexOf( app.getHomeScreen() ), (String[])screens.toArray() );
    }

    @OnClick( R.id.pref_btnResetApplication )
    public void onResetButtonCliced(){
        createConfirmationDialog( getString( R.string.reset ));
    }

    @Override
    public void onDialogPositiveClick( BaseDialog dialog, int id ){
        if( dialog.getType().equals( getString( R.string.minimum ) ) ){
            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
            app.setCreditRepresentationMin( Integer.parseInt( edit.getText().toString() ) );
        }else if( dialog.getType().equals( getString( R.string.maximum ) ) ){
            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
            app.setCreditRepresentationMax( Integer.parseInt( edit.getText().toString() ) );
        }else if( dialog.getType().equals( getString( R.string.language ) ) ){
            app.setLanguage( Language.DEFAULT );
            dialog.getDialog().cancel();
//        }else if( dialog.getType().equals( getString( R.string.home_screen ) ) ){
//            app.setHomeScreen( screens.get( id ) );
//            dialog.getDialog().cancel();
        }else if( dialog.getType().equals( getString( R.string.reset ) ) ){
            app.resetApp();
            main.finish();
            createConfirmationDialog( "App is reset" );
            System.exit( 0 );
        }
        updateView();
    }

    @Override
    public void onDialogNegativeClick( BaseDialog dialog ){
        dialog.getDialog().cancel();
    }

    private void createEditTextDialog( String type ){
        BaseDialog dialog = new EditTextDialog();
        dialog.setDialogEditInfo( type );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void createConfirmationDialog( String type ){
        BaseDialog dialog = new ConfirmationDialog();
        dialog.setDialogEditInfo( type );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void createListDialog( String type, int currentId, String[] options ){
        ListDialog dialog = new ListDialog();
        dialog.setOptions( options );
        dialog.setCurrent( currentId );
        dialog.setTargetFragment( this, 1 );
        dialog.setDialogEditInfo( type );
        dialog.show( getFragmentManager(), type );
    }
}
