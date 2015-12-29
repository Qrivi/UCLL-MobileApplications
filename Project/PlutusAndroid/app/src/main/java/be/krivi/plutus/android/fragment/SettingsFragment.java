package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.dialog.BaseDialog;
import be.krivi.plutus.android.dialog.ConfirmationDialog;
import be.krivi.plutus.android.dialog.EditTextDialog;
import be.krivi.plutus.android.dialog.RadioButtonDialog;
import be.krivi.plutus.android.view.CollapseAnimation;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Jan on 27/12/2015.
 */
public class SettingsFragment extends BaseFragment implements EditTextDialog.NoticeDialogListener{

    @Bind( R.id.pref_credit_gaugeMinMaxWrapper )
    LinearLayout mCreditMinMaxWrapper;

    @Bind( R.id.pref_credit_gaugeSwitch )
    SwitchCompat mCreditGaugeSwitch;

    @Bind( R.id.pref_notifications_switch )
    SwitchCompat mNotificationsSwitch;

    @Bind( R.id.pref_hintCreditGaugeMin )
    TextView mCreditGaugeMinHint;

    @Bind( R.id.pref_hintCreditGaugeMax )
    TextView mCreditGaugeMaxHint;

    @Bind( R.id.pref_application_languageHint )
    TextView mApplicationLanguageHint;

    @Bind( R.id.pref_application_homeScreenHint )
    TextView mApplicationHomeScreenHint;

    private Animation collapse, expand;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

       collapse = new CollapseAnimation(mCreditMinMaxWrapper, CollapseAnimation.CollapseAnimationAction.COLLAPSE);
        expand = new CollapseAnimation(mCreditMinMaxWrapper, CollapseAnimation.CollapseAnimationAction.EXPAND);

        updateView();
        return view;
    }

    private void updateView(){

        mCreditMinMaxWrapper.startAnimation( collapse );
        if( app.getCreditRepresentation() ){
            mCreditGaugeSwitch.setChecked( true );
            mCreditMinMaxWrapper.startAnimation( expand );
        }

        mApplicationHomeScreenHint.setText( getString( app.getHomeScreen().getId() ) );
        mCreditGaugeMinHint.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + app.getCreditRepresentationMin() );
        mCreditGaugeMaxHint.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + app.getCreditRepresentationMax() );
        mApplicationLanguageHint.setText( getString( app.getLanguage().getId() ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for CREDIT
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_credit_gaugeSwitchWrapper )
    public void onGaugeSwitchWrapperClicked(){
        mCreditGaugeSwitch.setChecked( !mCreditGaugeSwitch.isChecked() );
        onGaugeSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_credit_gaugeSwitch )
    public void onGaugeSwitchChanged(){
        app.setCreditRepresentation( mCreditGaugeSwitch.isChecked() );
        updateView();
    }

    @OnClick( R.id.pref_credit_gaugeMinWrapper )
    public void onWrapperGaugeMinClicked(){
        //createEditTextDialog( getString( R.string.minimum ), getString( R.string.setting_minimum_message ) );
    }

    @OnClick( R.id.pref_credit_gaugeMaxWrapper )
    public void onWrapperGaugeMaxClicked(){
        //createEditTextDialog( getString( R.string.maximum ), getString( R.string.settings_maximum_message ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for NOTIFICATIONS
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_notifications_switchWrapper )
    public void onNotificationSwitchClicked(){
        onNotificationsSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_notifications_switch )
    public void onNotificationsSwitchChanged(){
        mNotificationsSwitch.setChecked( false );
        Message.toast( app.getApplicationContext(), getString( R.string.not_in_beta ) );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for NOTIFICATIONS
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_application_languageWrapper )
    public void onLanguageWrapperClicked(){
        //createListDialog( getString( R.string.language ), languages.indexOf( app.getLanguage().toString() ), (int[])languages.toArray() );
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    // Listeners for APPLICATION
    ////////////////////////////////////////
    ////////////////////////////////////////

    @OnClick( R.id.pref_application_homeScreenWrapper )
    public void onHomeScreenClicked(){
        //createListDialog( getString( R.string.home_screen ), screens.indexOf( app.getHomeScreen() ), (Integer[])screens.toArray() );
    }

    @OnClick( R.id.pref_application_buttonReset )
    public void onResetButtonCliced(){
        //createConfirmationDialog( getString( R.string.reset ), getString( R.string.reset_message ) );
    }


    @Override
    public void onDialogPositiveClick( BaseDialog dialog, int id ){
        //        if( dialog.getType().equals( getString( R.string.minimum ) ) ){
        //            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
        //            app.setCreditRepresentationMin( Integer.parseInt( edit.getText().toString() ) );
        //        }else if( dialog.getType().equals( getString( R.string.maximum ) ) ){
        //            EditText edit = (EditText)dialog.getDialog().findViewById( R.id.dialog_edit );
        //            app.setCreditRepresentationMax( Integer.parseInt( edit.getText().toString() ) );
        //        }else if( dialog.getType().equals( getString( R.string.language ) ) ){
        //            app.setLanguage( Language.DEFAULT );
        //            dialog.getDialog().cancel();
        //        }else if( dialog.getType().equals( getString( R.string.home_screen ) ) ){
        //
        //            switch( screens.get( id ) ){
        //                case 0:
        //                    app.setHomeScreen( Window.CREDIT );
        //                    break;
        //                case 1:
        //                    app.setHomeScreen( Window.TRANSACTIONS );
        //                    break;
        //                case 2:
        //                    app.setHomeScreen( Window.SETTINGS );
        //                    break;
        //            }
        //            dialog.getDialog().cancel();
        //        }else if( dialog.getType().equals( getString( R.string.reset ) ) ){
        //            app.resetApp();
        //            main.finish();
        //            System.exit( 0 );
        //        }
        //        updateView();
    }

    @Override
    public void onDialogNegativeClick( BaseDialog dialog ){
        dialog.getDialog().cancel();
    }

    private void createEditTextDialog( String type, String message ){
        BaseDialog dialog = new EditTextDialog();
        dialog.setDialogEditInfo( type );
        dialog.setMessage( message );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void createConfirmationDialog( String type, String message ){
        BaseDialog dialog = new ConfirmationDialog();
        dialog.setDialogEditInfo( type );
        dialog.setMessage( message );
        dialog.setTargetFragment( this, 1 );
        dialog.show( getFragmentManager(), type );
    }

    private void createListDialog( String type, int currentId, int[] options ){
        RadioButtonDialog dialog = new RadioButtonDialog();
        dialog.setOptions( options );
        dialog.setCurrent( currentId );
        dialog.setTargetFragment( this, 1 );
        dialog.setDialogEditInfo( type );
        dialog.show( getFragmentManager(), type );
    }


}
