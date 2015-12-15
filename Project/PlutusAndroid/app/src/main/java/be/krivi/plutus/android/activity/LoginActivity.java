package be.krivi.plutus.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.network.retrofit.Client;
import be.krivi.plutus.android.network.retrofit.ServiceGenerator;
import be.krivi.plutus.android.rest.VerifyResponse;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

    private Client client;


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
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

        client = ServiceGenerator.createService( Client.class );
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
            mWrapperInput.startAnimation( aFadeOut );

            imm.toggleSoftInput( InputMethodManager.SHOW_FORCED, 0 );
            mTitle.setText( R.string.verifying_credentials );

            String studentId = mStudentId.getText().toString();
            String password = mPassword.getText().toString();

            String statusStudentId = app.verifyStudentId( studentId );
            String statusPassword = app.verifyPassword( password );

            if( statusStudentId.equals( "OK" ) && statusPassword.equals( "OK" ) ){

                Call<VerifyResponse> call = client.verify( studentId, password );
                call.enqueue( new Callback<VerifyResponse>(){
                    @Override
                    public void onResponse( Response<VerifyResponse> response, Retrofit retrofit ){
                        Log.v("RetroCool", response.message() + "  " + response.body() + response.headers().toString() );
                    }

                    @Override
                    public void onFailure( Throwable t ){
                        Log.v("RetroFail", "jammer" );

                    }
                } );





//                if( app.verifyCredentials( studentId, password ) ){
//                    mTitle.setText( R.string.populating_database );
//                    startActivity( new Intent( this, MainActivity.class ) );
//                    finish();// TODO app.logIn( id, pass )
//                    // nok -> showerror password
//                    // ok -> "populating databas" tot ...
//                }else{
//                    showError( "OK", getString( R.string.password_is_incorrect ) );
//                }
            }else{
                showError( statusStudentId, statusPassword );
            }
        }

    }

    private void showError( String errorStudentId, String errorPassword ){

        mWrapperInput.startAnimation( aFadeIn );

        mStudentIdStyle.setError( "" );
        mPasswordStyle.setError( "" );
        mPassword.setText( "" );
        mTitle.setText( R.string.sign_in_using_your_student_credentials );

        if( !errorStudentId.equals( "OK" ) )
            mStudentIdStyle.setError( errorStudentId );
        if( !errorPassword.equals( "OK" ) )
            mPasswordStyle.setError( errorPassword );

        busy = false;

    }
}
