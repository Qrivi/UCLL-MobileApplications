package be.krivi.plutus.android.network;

import be.krivi.plutus.android.activity.MyApplication;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jan on 8/12/2015.
 */
public class VolleySingleton{

    private static VolleySingleton sINSTANCE = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue( MyApplication.getAppContext());
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
