package be.krivi.plutus.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginActivity extends BaseActivity{

    InputMethodManager imm;

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

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        if( app.isUserRemembered() ){
            if( app.isNetworkAvailable() ){
                this.setContentView( R.layout.activity_login );
                showFadeOut( getResources().getString( R.string.populating_database ) );
                initializeBalance( app.getCurrentUser().getStudentId(), app.getCurrentUser().getPassword() );
            }else{
                initializeMainWindow();
            }
        }else{
            initializeLoginWindow();
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

            showFadeOut( getResources().getString( R.string.verifying_credentials ) );

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
        final String URL = Config.API_URL + Config.API_VERSION + "/verify";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        try{
                            JSONObject data = new JSONObject( response ).getJSONObject( "data" );
                            if( data.getBoolean( "valid" ) ){
                                app.initializeUser( studentId, password, data.getString( "firstName" ), data.getString( "lastName" ) );
                                initializeBalance( studentId, password );
                            }
                        }catch( JSONException e ){
                            // TODO write exception
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        showError( "OK", getResources().getString( R.string.password_is_incorrect ) );
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                return getCustomParams( studentId, password );
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return getCustomHeaders();
            }
        };
        request.setShouldCache( false );
        app.getRequestQueue().add( request );
    }

    private void initializeBalance( String studentId, String password ){
        mTitle.setText( getResources().getString( R.string.populating_database ) );
        balance( studentId, password );
    }

    private void balance( final String studentId, final String password ){
        final String URL = Config.API_URL + Config.API_VERSION + "/balance";

        StringRequest request = new StringRequest( Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        try{
                            JSONObject data = new JSONObject( response ).getJSONObject( "data" );
                            double balance = data.getDouble( "credit" );

                            app.getCurrentUser().setBalance( balance );
                            app.populateDatabase( 0 );

                            initializeMainWindow();
                        }catch( JSONException e ){
                            // TODO write exception
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        // TODO write exception
                        error.printStackTrace();
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                return getCustomParams( studentId, password );
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return getCustomHeaders();
            }
        };
        app.getRequestQueue().add( request );
    }

    private void showFadeOut( String text ){
        mWrapperInput.startAnimation( aFadeOut );

        imm.toggleSoftInput( InputMethodManager.SHOW_FORCED, 0 );
        mTitle.setText( text );
    }

    private void showError( String errorStudentId, String errorPassword ){

        mWrapperInput.startAnimation( aFadeIn );

        mStudentIdStyle.setError( "" );
        mPasswordStyle.setError( "" );
        mPassword.setText( "" );
        mTitle.setText( getResources().getString( R.string.sign_in_using_your_student_credentials ) );

        if( !errorStudentId.equals( "OK" ) )
            mStudentIdStyle.setError( errorStudentId );
        if( !errorPassword.equals( "OK" ) )
            mPasswordStyle.setError( errorPassword );

        busy = false;
    }

    private void initializeMainWindow(){
        startActivity( new Intent( app.getApplicationContext(), MainActivity.class ) );
        finish();
    }

    private void initializeLoginWindow(){
        this.setContentView( R.layout.activity_login );

        ButterKnife.bind( this );

        // TODO remove this
        mPassword.setText( "Pass1234" );
        mStudentId.setText( "r0123456" );

        imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE );

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
