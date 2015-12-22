package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import be.krivi.plutus.android.application.PlutusAndroid;

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
