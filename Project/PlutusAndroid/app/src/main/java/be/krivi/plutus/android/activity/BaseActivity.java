package be.krivi.plutus.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import be.krivi.plutus.android.application.PlutusAndroid;

/**
 * Created by Krivi on 14/12/15.
 */
public class BaseActivity extends AppCompatActivity{

    PlutusAndroid app;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        app = (PlutusAndroid) this.getApplicationContext();

        app.setCurrentActivity(this);
    }

    protected void OnPause(){
        app.setCurrentActivity( null );
    }
}
