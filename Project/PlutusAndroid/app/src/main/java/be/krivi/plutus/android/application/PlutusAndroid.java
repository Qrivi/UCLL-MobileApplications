package be.krivi.plutus.android.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.BaseActivity;
import be.krivi.plutus.android.io.IOService;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.model.User;
import be.krivi.plutus.android.network.volley.NetworkClient;
import be.krivi.plutus.android.network.volley.VolleySingleton;
import com.android.volley.RequestQueue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Krivi on 09/12/15.
 */
public class PlutusAndroid extends Application{

    private static PlutusAndroid instance;
    private BaseActivity currentActivity;
    private User user;

    private IOService IOService;
    private NetworkClient networkClient;

    private RequestQueue mRequestQueue;


    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        this.mRequestQueue = VolleySingleton.getINSTANCE().getmRequestQueue();
        IOService = new IOService( getAppContext() );
        networkClient = new NetworkClient();
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }


    public void saveCredentials() {
        IOService.saveCredentials( getCurrentUser() );
    }

    public void initializeUser( String studentId, String password, String firstname, String lastname ){
        this.user = new User( studentId, password, firstname, lastname );
    }

    public void initializeUserBalance( double balance ){
        this.user.setBalance( balance );
    }

    public User getCurrentUser(){
        return user;
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


    public boolean isUserRemembered(){
        boolean isRemembered = IOService.isUserRemembered();
        if( isRemembered ){
            try{
                user = new User(IOService.getStudentId(), IOService.getPassword());
                initializeUserBalance( IOService.getBalance() );
            }catch( IOException e ){
                isRemembered = false;
            }
        }
        return isRemembered;
    }

    public String getRememberedStudentId() throws IOException{
        return IOService.getStudentId();
    }

    public String getRemberedPassword() throws IOException{
        return IOService.getPassword();
    }

    public boolean isNetworkAvailable(){
        final ConnectivityManager connectivityManager = ( (ConnectivityManager)getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE ) );
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    public void populateDatabase(int page) {
        networkClient.populateDatabase( user, page );
    }

    public List<Transaction> getTransactions() throws ParseException{
        return IOService.getAllTransactions();
    }
}
