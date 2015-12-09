package be.krivi.plutus.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.network.Client;
import be.krivi.plutus.android.view.Message;

public class LoginActivity extends Activity implements View.OnClickListener{

    private ImageView ucllLogo;
    private LinearLayout loginForm;
    private Button loginButton;
    private ImageButton aboutButton;

    private EditText studentId;
    private EditText password;
    private CheckBox rememberMeBox;

    private LinearLayout progressFrame;
    private TextView progressText;

    private Client client;

    private boolean rememberMe;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_login );

        ucllLogo = (ImageView)findViewById( R.id.ucllLogo );
        loginForm = (LinearLayout)findViewById( R.id.loginForm );
        loginButton = (Button)findViewById( R.id.btn_logIn );
        aboutButton = (ImageButton)findViewById( R.id.btn_aboutPlutus );

        studentId = (EditText)findViewById( R.id.txt_studentId );
        password = (EditText)findViewById( R.id.txt_password );
        rememberMeBox = (CheckBox)findViewById( R.id.cbx_rememberMe );

        progressFrame = (LinearLayout)findViewById( R.id.progressFrame );
        progressText = (TextView)findViewById( R.id.progressText );

        client = new Client(this);

        loginButton.setOnClickListener( this );
        aboutButton.setOnClickListener( this );
    }

    @Override
    public void onClick( View view ){
        switch( view.getId() ){
            case R.id.btn_aboutPlutus:
                startActivity( new Intent( this, BrowserActivity.class ) );
                break;
            case R.id.btn_logIn:
                verifyCredentials();
                break;
            default:
                Message.toast( this, view.toString() );
                break;
        }
    }

    private void verifyCredentials(){

        client.send();
        hideLoginForm();
        progressText.setText( "Verifying credentials..." );
    }

    private boolean autoLogin(){

        return false;
    }

    private void showLoginForm(){
        ucllLogo.setVisibility( View.VISIBLE );
        aboutButton.setVisibility( View.VISIBLE );
        loginButton.setVisibility( View.VISIBLE );
        loginForm.setVisibility( View.VISIBLE );
        progressFrame.setVisibility( View.GONE );

    }

    private void hideLoginForm(){
        ucllLogo.setVisibility( View.GONE );
        aboutButton.setVisibility( View.GONE );
        loginButton.setVisibility( View.GONE );
        loginForm.setVisibility( View.GONE );
        progressFrame.setVisibility( View.VISIBLE );

    }

}
