package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.network.volley.VolleyCallback;
import be.krivi.plutus.android.view.Message;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan on 22/12/2015.
 */

public class BaseFragment extends Fragment{

    PlutusAndroid app;

    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        app = (PlutusAndroid)PlutusAndroid.getAppContext();
    }


}
