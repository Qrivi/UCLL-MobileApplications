package be.krivi.plutus.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class LoginActivity extends Activity{

    @Override
    protected void onCreate( Bundle savedInstanceState ){

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
    }
}
