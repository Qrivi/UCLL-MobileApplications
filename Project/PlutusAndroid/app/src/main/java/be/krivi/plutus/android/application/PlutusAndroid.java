package be.krivi.plutus.android.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.BaseActivity;
import be.krivi.plutus.android.model.User;
import be.krivi.plutus.android.network_deprecated.Client;

/**
 * Created by Krivi on 09/12/15.
 */
public class PlutusAndroid extends Application{

    private static PlutusAndroid instance;
    private BaseActivity currentActivity;
    private Client client;
    private User user;


    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        client = new Client();
    }

    public static PlutusAndroid getInstance(){
        return instance;
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }

    public BaseActivity getCurrentActivity(){
        return this.currentActivity;
    }

    public void setCurrentActivity( BaseActivity activity ){
        this.currentActivity = activity;
    }

    public String getProjectUri(){
        return Config.PROJECT_URL;
    }

    public String verifyStudentId( String studentId ){
        if( currentActivity.getLocalClassName().equals( "activity.LoginActivity" ) ){

            if( studentId.equals( "" ) )
                return getString( R.string.this_field_is_required );
            else if( studentId.length() < 8 )
                return getString( R.string.this_id_is_too_short );
            else if( !studentId.toLowerCase().matches( "^[a-zA-Z][0-9]{7}$" ) )
                return getString( R.string.this_id_does_not_exist );

            return "OK";

        }else{
            Log.e( "Plutus internal error", "Trying to verify credentials but user is not on LoginActivity." );
            return "";
        }
    }

    public String verifyPassword( String password ){
        if( currentActivity.getLocalClassName().equals( "activity.LoginActivity" ) ){

            if( password.equals( "" ) )
                return getString( R.string.this_field_is_required );

            return "OK";

        }else{
            Log.e( "PlutusAndroid", "Trying to verify credentials but user is not in LoginActivity" );
            return "";
        }
    }

    public boolean verifyCredentials( String studentId, String password ){
        user = client.verify( studentId, password );

        Log.v("verifycre", "" + user.getStudentId());
        return !(user.getStudentId() == null);
    }

}
