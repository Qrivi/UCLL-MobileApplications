package be.krivi.plutus.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.network.volley.VolleyCallback;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity{



    Animation aFadeIn;
    Animation aFadeOut;

    boolean busy;

    @Bind( R.id.wrapper )
    RelativeLayout mWrapper;

    @Bind( R.id.wrapperInput )
    LinearLayout mWrapperInput;

    @Bind( R.id.ucllLogo )
    ImageView mUcllLogo;

    @Bind( R.id.title )
    TextView mTitle;

    @Bind( R.id.txt_studentId )
    EditText mStudentId;

    @Bind( R.id.txt_password )
    EditText mPassword;

    @Bind( R.id.studentIdStyle )
    TextInputLayout mStudentIdStyle;

    @Bind( R.id.passwordStyle )
    TextInputLayout mPasswordStyle;

    @Bind( R.id.btn_signIn )
    Button mBtn_signIn;

    @Bind( R.id.btn_tryAgain )
    Button mBtn_tryAgain;


    @Override
    protected void onCreate( Bundle savedInstanceState ){

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        ButterKnife.bind( this );

        if( app.isUserSaved() ){
            initializeMainWindow();
        }else{
            initializeLoginWindow();
            if( !app.isNetworkAvailable() ){
                mBtn_tryAgain.setVisibility( View.VISIBLE );
                mWrapperInput.setVisibility( View.INVISIBLE );
                mTitle.setText( R.string.there_is_no_active_internet_connection );
            }
        }

    }

    @OnClick( R.id.btn_tryAgain )
    public void tryAgainClickHandler(){
        if( app.isNetworkAvailable() ){
            mBtn_tryAgain.setVisibility( View.INVISIBLE );
            mWrapperInput.setVisibility( View.VISIBLE );
            mTitle.setText( R.string.sign_in_using_your_student_credentials );
        }
    }

    @OnClick( R.id.btn_info )
    public void infoClickHandler(){
        startActivity( new Intent( Intent.ACTION_VIEW ).setData( Uri.parse( app.getProjectUri() ) ) );
    }

    @OnClick( R.id.btn_signIn )
    public void signInClickHandler(){
        verifyCredentials();
    }

    private void verifyCredentials(){
        if( !busy ){
            busy = true;

            String studentId = mStudentId.getText().toString().toLowerCase();
            String password = mPassword.getText().toString();

            String statusStudentId = app.verifyStudentId( studentId );
            String statusPassword = app.verifyPassword( password );

            if( statusStudentId.equals( "OK" ) && statusPassword.equals( "OK" ) )
                verifyCredentials( studentId, password );
            else
                showError( statusStudentId, statusPassword );
        }
    }

    private void verifyCredentials( final String studentId, final String password ){

        showFadeOut( getString( R.string.verifying_credentials ) );

        Map<String, String> params = new HashMap<>();
        params.put( "studentId", studentId );
        params.put( "password", password );

        app.contactAPI( params, Config.API_ENPOINT_VERIFY, new VolleyCallback(){
            @Override
            public void onSuccess( String response ){
                try{
                    JSONObject data = new JSONObject( response ).getJSONObject( "data" );
                    if( data.getBoolean( "valid" ) ){
                        app.initializeUser( studentId, password, data.getString( "firstName" ), data.getString( "lastName" ) );
                        initializeMainWindow();
                    } else {
                        showError("OK", getString( R.string.password_is_incorrect ));
                    }
                }catch( JSONException e ){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure( VolleyError error ){
                Message.obtrusive( app.getApplicationContext(), error.getMessage() );
            }
        } );
    }

    private void showFadeOut( String text ){
        mWrapperInput.startAnimation( aFadeOut );


       imm.toggleSoftInput( InputMethodManager.HIDE_IMPLICIT_ONLY, 0 );

        mTitle.setText( text );
    }

    private void showError( String errorStudentId, String errorPassword ){

        mWrapperInput.startAnimation( aFadeIn );

        mStudentIdStyle.setError( "" );
        mPasswordStyle.setError( "" );
        mPassword.setText( "" );
        mTitle.setText( getString( R.string.sign_in_using_your_student_credentials ) );

        if( !errorStudentId.equals( "OK" ) )
            mStudentIdStyle.setError( errorStudentId );
        if( !errorPassword.equals( "OK" ) )
            mPasswordStyle.setError( errorPassword );

        busy = false;
    }

    private void initializeMainWindow(){
        app.loadData();
        startActivity( new Intent( this, MainActivity.class ) );
        finish();
    }

    private void initializeLoginWindow(){

        // TODO remove this
         mPassword.setText( "Pass1234" );
         mStudentId.setText( "r0123456" );

        if( !app.isNewInstallation() )
            mStudentId.setText( app.getStudentId() );

        aFadeIn = AnimationUtils.loadAnimation( app, R.anim.fade_in );
        aFadeOut = AnimationUtils.loadAnimation( app, R.anim.fade_out );

        mPasswordStyle.setTypeface( Typeface.SANS_SERIF );
        mPassword.setOnEditorActionListener( new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction( TextView v, int actionId, KeyEvent event ){
                verifyCredentials();
                return true;
            }
        } );
    }
}
