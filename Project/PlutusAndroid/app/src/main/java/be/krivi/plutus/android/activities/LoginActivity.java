package be.krivi.plutus.android.activities;

import android.app.Activity;
import android.os.Bundle;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 27/11/2015.
 */
public class LoginActivity  extends BaseActivity{


    @Override
    protected void onCreate( Bundle savedInstanceState ){

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
    }

    @Override
    protected int onCreateViewId(){
        return R.layout.activity_login;
    }

    @Override
    protected int onCreateViewToolbarId(){
        return 0;
    }


}
