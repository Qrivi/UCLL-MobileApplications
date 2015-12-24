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
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.*;

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

        homeScreen = IOService.getHomeScreen().equals( "" ) ? Config.SETTINGS_DEFAULT_HOMESCREEN : IOService.getHomeScreen();
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
        loadData();
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

    public boolean isNewInstallation(){
        return IOService.isNewInstallation();
    }

    public void loadData(){
        user = new User( IOService.getStudentId(), IOService.getPassword(), IOService.getFirstname(),
                IOService.getLastname(), IOService.getBalance() );
        try{
            transactions = IOService.getAllTransactions();
        }catch( ParseException e ){
            Message.obtrusive( currentActivity, getString( R.string.error_loading_data_into_app ) + e.getMessage() );
        }
    }

    public void logoutUser(){
        IOService.cleanSharedPreferences();
        IOService.cleanDatabase();
    }


    public boolean isNetworkAvailable(){
        final ConnectivityManager connectivityManager = ( (ConnectivityManager)getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE ) );
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void contactAPI( Map<String, String> params, String endpoint, final VolleyCallback callback ){
        networkClient.contactAPI( params, endpoint, callback );
    }

    public boolean writeTransactions( JSONArray JSONTransactions ){
        boolean writeSuccessful = IOService.writeTransactions( JSONTransactions );
        loadData();
        return writeSuccessful;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

    public List<Transaction> getTransactions( int entries ){
        entries = entries > transactions.size() ? transactions.size() : entries;
        return transactions.subList( 0, entries );
    }

    public List<Transaction> getTransactionsSet( int set ){

        int start = set * Config.APP_DEFAULT_LIST_SIZE;
        if( start > transactions.size() )
            return null;

        int end = start + Config.APP_DEFAULT_LIST_SIZE < transactions.size() ? start + Config.APP_DEFAULT_LIST_SIZE : transactions.size();
        return transactions.subList( start, end );
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

        return now.after( cal.getTime() );
    }

    public void savePauseTimestamp( Date timestamp ){
        IOService.savePauseTimestamp( timestamp );
    }

    public String getStudentId(){
        return IOService.getStudentId();
    }

    public void completeDatabase( final int page ){

        if( isNetworkAvailable() ){
            Map<String, String> params = new HashMap<>();
            params.put( "studentId", getCurrentUser().getStudentId() );
            params.put( "password", getCurrentUser().getPassword() );

            contactAPI( params, Config.API_ENDPOINT_TRANSACTIONS + "/" + page, new VolleyCallback(){
                @Override
                public void onSuccess( String response ){
                    try{
                        JSONArray array = new JSONObject( response ).getJSONArray( "data" );
                        if( writeTransactions( array ) )
                            completeDatabase( ( page ) + 1 );
                    }catch( JSONException e ){
                        try{
                            JSONObject obj = new JSONObject( response );
                            if( !obj.has( "data" ) )
                                throw new JSONException( "Response did not contain any data" );
                            Message.snack( currentActivity.findViewById( android.R.id.content ), getString( R.string.database_updated ) );
                        }catch( JSONException f ){
                            Message.obtrusive( getCurrentActivity(), getString( R.string.error_fetching_transactions ) + e.getMessage() );
                        }
                    }
                }

                @Override
                public void onFailure( VolleyError error ){
                    Message.obtrusive( getCurrentActivity(), getString( R.string.error_contacting_api ) + error.getMessage() );
                }
            } );
        }

    }
}
