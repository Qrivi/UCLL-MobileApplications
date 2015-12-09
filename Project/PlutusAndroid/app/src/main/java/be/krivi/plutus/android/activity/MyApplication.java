package be.krivi.plutus.android.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jan on 8/12/2015.
 */
public class MyApplication extends Application{

    private static MyApplication sInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


}
