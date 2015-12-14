package be.krivi.plutus.android.network;

import be.krivi.plutus.android.application.PlutusAndroid;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jan on 8/12/2015.
 */
public class VolleySingleton{

    private static VolleySingleton instance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue( PlutusAndroid.getAppContext() );
    }

    public static VolleySingleton getINSTANCE(){
        if( instance == null )
            instance = new VolleySingleton();
        return instance;
    }

    public RequestQueue getmRequestQueue(){
        return mRequestQueue;
    }
}
