package be.krivi.plutus.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.PlutusAndroid;
import com.android.volley.AuthFailureError;

import java.util.HashMap;
import java.util.Map;

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

    protected Map<String, String> getCustomHeaders() throws AuthFailureError{
        Map<String, String> headers = new HashMap<>();
        String credentials = Config.API_LOGIN + ":" + Config.API_PASSWORD;
        String auth = "Basic "
                + Base64.encodeToString( credentials.getBytes(),
                Base64.NO_WRAP );
        headers.put( "Authorization", auth );
        return headers;
    }

    protected Map<String, String> getCustomParams( String studentID, String password ) throws AuthFailureError{
        Map<String, String> params = new HashMap<String, String>();
        params.put( "studentId", studentID );
        params.put( "password", password );
        return params;
    }



}
