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
import be.krivi.plutus.android.network.volley.VolleyCallback;
import be.krivi.plutus.android.view.Message;
import org.json.JSONArray;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Krivi on 09/12/15.
 */
public class PlutusAndroid extends Application{

    private static PlutusAndroid instance;
    private BaseActivity currentActivity;

    private User user;
    private List<Transaction> transactions;

    private IOService IOService;
    private NetworkClient networkClient;

    private String homeScreen;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        IOService = new IOService( getAppContext() );
        networkClient = new NetworkClient();

        homeScreen = IOService.getHomeScreen().equals( "" ) ? Config.APP_DEFAULT_HOMESCREEN : IOService.getHomeScreen();
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }


    public void initializeUser( String studentId, String password, String firstname, String lastname ){
        this.user = new User( studentId, password, firstname, lastname );
        IOService.saveCredentials( getCurrentUser() );
    }

    public void initializeUserBalance( double balance ){
        this.user.setBalance( balance );
        IOService.saveBalance( balance );
    }


    public BaseActivity getCurrentActivity(){
        return currentActivity;
    }

    public void setCurrentActivity( BaseActivity activity ){
        this.currentActivity = activity;
    }

    public User getCurrentUser(){
        return user;
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

    public boolean isUserSaved(){

        return IOService.isUserSaved();
    }

    public String existsStudentId() {
        return IOService.existsStudentId();
    }

    public void loadData(){
        user = new User( IOService.getStudentId(), IOService.getPassword(), IOService.getFirstname(),
                IOService.getLastname(), IOService.getBalance() );
        try{
            transactions = IOService.getAllTransactions();
        }catch( ParseException e ){
            Message.obtrusive( currentActivity, "Error loading data into application: \n" + e.getMessage() );
        }
    }

    public void logoutUser() {
        IOService.clearSharedPreferences();
        IOService.clearDatabase();
    }


    public boolean isNetworkAvailable(){
        final ConnectivityManager connectivityManager = ( (ConnectivityManager)getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE ) );
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void contactAPI( Map<String, String> params, String endpoint, final VolleyCallback callback ){
        networkClient.contactAPI( params, endpoint, callback );
    }

    public void writeTransactions( JSONArray JSONTransactions ){
        IOService.writeTransactions( JSONTransactions );
        loadData();
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public String getHomeScreen(){

        return homeScreen;
    }

    public void setHomeScreen( String homeScreen ){
        this.homeScreen = homeScreen;
        IOService.saveHomeScreen( homeScreen );
    }

    public boolean fetchRequired(){
        // if pauseTime was longer than 1 hour ago

        Date pauseDate = IOService.getPauseTimestamp();
        if( pauseDate == null )
            return true;

        Date now = new Date( System.currentTimeMillis() );

        Calendar cal = Calendar.getInstance();
        cal.setTime( pauseDate );
        cal.add( Calendar.MINUTE, Config.APP_DEFAULT_SNOOZE_TIME );

        Log.v( "Now", now.toString() );
        Log.v("pause", cal.getTime().toString());

        return now.after( cal.getTime() );
    }

    public void savePauseTimestamp( Date timestamp ){
        IOService.savePauseTimestamp( timestamp );
    }
}
