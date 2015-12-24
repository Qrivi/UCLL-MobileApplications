package be.krivi.plutus.android.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.network.volley.VolleyCallback;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

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
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        app = (PlutusAndroid)this.getApplicationContext();
    }

    @Override
    protected void onPause(){
        super.onPause();
        app.setCurrentActivity( null );
    }
    @Override
    protected void onResume() {
        super.onResume();
        app.setCurrentActivity( this );
    }



}
