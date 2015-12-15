package be.krivi.plutus.android.network.volley;


import be.krivi.plutus.android.application.PlutusAndroid;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jan on 8/12/2015.
 */
public class    VolleySingleton{

    private static VolleySingleton sINSTANCE = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(PlutusAndroid.getAppContext());
    }

    public static VolleySingleton getINSTANCE(){
        if( sINSTANCE == null ){
            sINSTANCE = new VolleySingleton();
        }
        return sINSTANCE;
    }
    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }
}
