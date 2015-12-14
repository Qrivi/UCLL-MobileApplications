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
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        this.setContentView( R.layout.activity_login );
        ButterKnife.bind( this );

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

            String statusStudentId = app.verifyStudentId( mStudentId.getText().toString() );
            String statusPassword = app.verifyPassword( mPassword.getText().toString() );

            if( statusStudentId.equals( "OK" ) && statusPassword.equals( "OK" ) ){
                // TODO app.logIn( id, pass )
                // nok -> showerror password
                // ok -> "populating databas" tot ...
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else{
                mWrapperInput.startAnimation( aFadeIn );

                mStudentIdStyle.setError( "" );
                mPasswordStyle.setError( "" );
                mPassword.setText( "" );
                mTitle.setText( R.string.sign_in_using_your_student_credentials );

                if( !statusStudentId.equals( "OK" ) )
                    mStudentIdStyle.setError( statusStudentId );
                if( !statusPassword.equals( "OK" ) )
                    mPasswordStyle.setError( statusPassword );

                busy = false;
            }
        }
    }
}
