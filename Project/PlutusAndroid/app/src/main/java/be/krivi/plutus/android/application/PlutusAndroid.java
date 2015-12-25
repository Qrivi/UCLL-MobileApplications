package be.krivi.plutus.android.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.BaseActivity;
import be.krivi.plutus.android.activity.MainActivity;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Krivi on 09/12/15.
 */
public class PlutusAndroid extends Application{

    private static PlutusAndroid instance;
    private BaseActivity currentActivity;

    private User user;
    private List<Transaction> transactions;

    private IOService ioService;
    private NetworkClient networkClient;

    private String homeScreen;
    DateFormat format;

    boolean databaseIncomplete;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        ioService = new IOService( getAppContext() );
        networkClient = new NetworkClient();
        format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ", Locale.US );

        homeScreen = ioService.getHomeScreen().equals( "" ) ? Config.SETTINGS_DEFAULT_HOMESCREEN : ioService.getHomeScreen();
        databaseIncomplete = ioService.isDatabaseIncomplete();
    }

    public static Context getAppContext(){
        return instance.getApplicationContext();
    }


    public void initializeUser( String studentId, String password, String firstname, String lastname ){
        this.user = new User( studentId, password, firstname, lastname );
        ioService.saveCredentials( getCurrentUser() );
    }

    public void writeUserCredit( double credit, String fetchDate ){

        try{
            Date date = format.parse( fetchDate );

            this.user.setCredit( credit );
            ioService.saveCredit( credit );
            this.user.setFetchDate( date );
            ioService.saveFetchDate( date );
            loadData();
        }catch( ParseException e ){
            Message.obtrusive( currentActivity, getString( R.string.error_loading_data_into_app ) + e.getMessage() );
        }
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

    public String getProjectUrl(){
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

        return ioService.isUserSaved();
    }

    public boolean isNewInstallation(){
        return ioService.isNewInstallation();
    }

    public boolean isDatabaseIncomplete(){
        return ioService.isDatabaseIncomplete();
    }

    public void loadData(){
        try{
            user = new User(
                    ioService.getStudentId(), ioService.getPassword(),
                    ioService.getFirstname(), ioService.getLastname(),
                    ioService.getCredit(), ioService.getFetchDate() );
            transactions = ioService.getAllTransactions();
        }catch( ParseException e ){
            Message.obtrusive( currentActivity, getString( R.string.error_loading_data_into_app ) + e.getMessage() );
        }
    }

    public void logoutUser(){
        ioService.cleanSharedPreferences();
        ioService.cleanDatabase();
    }


    public boolean isNetworkAvailable(){
        final ConnectivityManager connectivityManager = ( (ConnectivityManager)getAppContext().getSystemService( Context.CONNECTIVITY_SERVICE ) );
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void contactAPI( Map<String, String> params, String endpoint, final VolleyCallback callback ){
        networkClient.contactAPI( params, endpoint, callback );
    }

    public boolean writeTransactions( JSONArray JSONTransactions ){
        boolean writeSuccessful = ioService.writeTransactions( JSONTransactions );
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
        ioService.saveHomeScreen( homeScreen );
    }

    public boolean fetchRequired(){
        // if pauseTime was earlier than 1 hour ago

        Date pauseDate = ioService.getFetchDate();
        if( pauseDate == null ){
            Log.v( "Data status", "empty preferences -- no data" );
            ioService.saveNewInstallation( false );
            return true;
        }

        Date now = new Date( System.currentTimeMillis() );

        Calendar cal = Calendar.getInstance();
        cal.setTime( pauseDate );
        cal.add( Calendar.MINUTE, Config.APP_DEFAULT_SNOOZE_TIME );

        Log.v( "Data status", "now: " + now );
        Log.v( "Data status", "last: " + pauseDate );
        Log.v( "Data status", "snooze: " + cal.getTime() );
        Log.v( "Data status", "fetch required: " + now.after( cal.getTime() ) );

        return now.after( cal.getTime() );
    }

    public String getStudentId(){
        return ioService.getStudentId();
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
                        if( writeTransactions( array ) && databaseIncomplete ){
                            int nextPage = page + 1;
                            completeDatabase( nextPage );
                        }else{
                            MainActivity main = (MainActivity)currentActivity;
                            Message.snack( main.mDrawerLayout, getString( R.string.database_updated ) );
                            ioService.saveDatabaseIncomplete( databaseIncomplete = false );
                            Log.v( "Data status", "refreshed -- saved to db" );
                            return; // safety first
                        }
                    }catch( JSONException e ){
                        try{
                            JSONObject obj = new JSONObject( response );
                            if( !obj.has( "data" ) )
                                throw new JSONException( "Response did not contain any data" );
                            MainActivity main = (MainActivity)currentActivity;
                            Message.snack( main.mDrawerLayout, getString( R.string.database_updated ) );
                            ioService.saveDatabaseIncomplete( databaseIncomplete = false );
                            Log.v( "Data status", "refreshed -- saved to db" );
                        }catch( JSONException f ){
                            Message.obtrusive( getCurrentActivity(), getString( R.string.error_fetching_transactions ) + e.getMessage() );
                        }
                    }
                }

                @Override
                public void onFailure( VolleyError error ){
                    Message.obtrusive( getCurrentActivity(), getString( R.string.error_endpoint_transactions ) );
                }
            } );
        }

    }
}
