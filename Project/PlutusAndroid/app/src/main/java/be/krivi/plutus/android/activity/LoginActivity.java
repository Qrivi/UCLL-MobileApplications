package be.krivi.plutus.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.view.Message;

public class LoginActivity extends Activity implements CompoundButton.OnCheckedChangeListener{

    private CheckBox rememberMe;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_login );

        rememberMe = (CheckBox)findViewById( R.id.checkbox_rememberMe );
        rememberMe.setOnCheckedChangeListener( this );


        ImageButton btn = (ImageButton)findViewById(R.id.btn_aboutPlutus );

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick( View view ){
                Intent intent = new Intent( LoginActivity.this, BrowserActivity.class );
                startActivity( intent );
            }
        });

        }

    @Override
    public void onCheckedChanged( CompoundButton compoundButton, boolean b ){
        Message.toast( this, "ayy lmao" );
    }
}
