package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.Language;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.application.Window;
import be.krivi.plutus.android.dialog.BaseDialog;
import be.krivi.plutus.android.dialog.ConfirmationDialog;
import be.krivi.plutus.android.dialog.EditTextDialog;
import be.krivi.plutus.android.dialog.ListDialog;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import java.util.ArrayList;
import java.util.LinkedList;
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

    private List<Integer> languages;
    private List<Integer> screens;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_settings, container, false );
        ButterKnife.bind( this, view );

        main = (MainActivity)getActivity();
        app = (PlutusAndroid)main.getApplication();

        languages = new LinkedList<>();
        screens = new LinkedList<>();

        languages.add( Language.DEFAULT.getId());
        languages.add( Language.DUTCH.getId() );
        languages.add( Language.FRENCH.getId() );
        languages.add( Language.ENGLISH.getId() );

        screens.add( Window.CREDIT.getReference() );
        screens.add( Window.TRANSACTIONS.getReference() );
        screens.add( Window.SETTINGS.getReference() );

        updateView();
        return view;
    }

    private void updateView(){
        boolean gaugeChecked = app.getCreditRepresentation();

        if( gaugeChecked )
            expand();
        else
            collapse();

        switchCreditGauge.setChecked( gaugeChecked );
        hintGaugeMin.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + app.getCreditRepresentationMin() );
        hintGaugeMax.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + app.getCreditRepresentationMax() );
        hintLanguage.setText( app.getLanguage().toString() );
        hintHomescreen.setText( getString( app.getHomeScreen().getReference() ) );
    }

    @OnClick( R.id.pref_wrapperCredit_gaugeSwitch )
    public void onGaugeSwitchWrapperClicked(){
        switchCreditGauge.setChecked( !switchCreditGauge.isChecked() );
        onGaugeSwitchChanged();
    }

    @OnCheckedChanged( R.id.pref_switchCreditGauge )
    public void onGaugeSwitchChanged(){
        app.setCreditRepresentation( switchCreditGauge.isChecked() );
        updateView();
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
        createEditTextDialog( getString( R.string.minimum ), getString( R.string.setting_minimum_message ) );
    }

    @OnClick( R.id.pef_wrapperCredit_gaugeMax )
    public void onWrapperGaugeMaxClicked(){
        createEditTextDialog( getString( R.string.maximum ), getString( R.string.settings_maximum_message ) );
    }

    @OnClick( R.id.pref_wrapperNotifications_language )
    public void onLanguageWrapperClicked(){
        createListDialog( getString( R.string.language ), languages.indexOf( app.getLanguage().toString() ), (int[])languages.toArray() );
    }

    @OnClick( R.id.pref_wrapperHomeScreen )
    public void onHomeScreenClicked(){
        createListDialog( getString( R.string.home_screen ), screens.indexOf( app.getHomeScreen() ), (Integer[]) screens.toArray() );
    }

    @OnClick( R.id.pref_btnResetApplication )
    public void onResetButtonCliced(){
        createConfirmationDialog( getString( R.string.reset ), getString( R.string.reset_message ) );
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
        }else if( dialog.getType().equals( getString( R.string.home_screen ) ) ){

            switch( screens.get( id ) ) {
                case 0:
                    app.setHomeScreen( Window.CREDIT );
                    break;
                case 1:
                    app.setHomeScreen( Window.TRANSACTIONS );
                    break;
                case 2:
                    app.setHomeScreen( Window.SETTINGS );
                    break;
            }
            dialog.getDialog().cancel();
        }else if( dialog.getType().equals( getString( R.string.reset ) ) ){
            app.resetApp();
            main.finish();
            System.exit( 0 );
        }
        updateView();
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
        ListDialog dialog = new ListDialog();
        dialog.setOptions( options );
        dialog.setCurrent( currentId );
        dialog.setTargetFragment( this, 1 );
        dialog.setDialogEditInfo( type );
        dialog.show( getFragmentManager(), type );
    }

    public void expand(){

        wrapperGaugeMin.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        wrapperGaugeMin.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

        final int targetHeigtMin = wrapperGaugeMin.getMeasuredHeight();
        final int targetHeigtMax = wrapperGaugeMin.getMeasuredHeight();

        wrapperGaugeMin.getLayoutParams().height = 1;
        wrapperGaugeMin.setVisibility( View.VISIBLE );

        wrapperGaugeMax.getLayoutParams().height = 1;
        wrapperGaugeMax.setVisibility( View.VISIBLE );

        Animation a = new Animation(){
            @Override
            protected void applyTransformation( float interpolatedTime, Transformation t ){
                wrapperGaugeMin.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)( targetHeigtMin * interpolatedTime );
                wrapperGaugeMin.requestLayout();

                wrapperGaugeMax.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)( targetHeigtMax * interpolatedTime );
                wrapperGaugeMax.requestLayout();
            }

            @Override
            public boolean willChangeBounds(){
                return true;
            }
        };

        // 1dp/ms
        a.setDuration( (int)( targetHeigtMin / wrapperGaugeMin.getContext().getResources().getDisplayMetrics().density ) );
        a.setDuration( (int)( targetHeigtMax / wrapperGaugeMax.getContext().getResources().getDisplayMetrics().density ) );
        wrapperGaugeMin.startAnimation( a );
        wrapperGaugeMax.startAnimation( a );
    }

    public void collapse(){

        final int initialHeightMin = wrapperGaugeMin.getMeasuredHeight();
        final int initialHeightMax = wrapperGaugeMax.getMeasuredHeight();

        Animation a = new Animation(){
            @Override
            protected void applyTransformation( float interpolatedTime, Transformation t ){
                if( interpolatedTime == 1 ){
                    wrapperGaugeMin.setVisibility( View.GONE );
                    wrapperGaugeMax.setVisibility( View.GONE );
                }else{
                    wrapperGaugeMin.getLayoutParams().height = initialHeightMin - (int)( initialHeightMin * interpolatedTime );
                    wrapperGaugeMax.getLayoutParams().height = initialHeightMax - (int)( initialHeightMax * interpolatedTime );
                    wrapperGaugeMin.requestLayout();
                    wrapperGaugeMax.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds(){
                return true;
            }
        };

        // 1dp/ms
        a.setDuration( (int)( initialHeightMin / wrapperGaugeMin.getContext().getResources().getDisplayMetrics().density ) );
        a.setDuration( (int)( initialHeightMax / wrapperGaugeMax.getContext().getResources().getDisplayMetrics().density ) );
        wrapperGaugeMin.startAnimation( a );
        wrapperGaugeMax.startAnimation( a );
    }

}
