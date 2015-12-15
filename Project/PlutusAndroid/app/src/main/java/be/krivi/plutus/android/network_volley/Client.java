package be.krivi.plutus.android.network_volley;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.model.User;
import be.krivi.plutus.android.view.Message;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jan on 8/12/2015.
 */
public class Client{

    private RequestQueue mRequestQueue;
    private Context context;

    public Client(){
        this.mRequestQueue = VolleySingleton.getINSTANCE().getmRequestQueue();
        this.context = PlutusAndroid.getAppContext();
    }

    public void transactions( final User user, final int page ){
        final String URL = Config.API_URL + Config.API_VERSION + "/transactions";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        try{
                            JSONObject data = new JSONObject( response ).getJSONObject( "transactions" );
                            JSONArray array = data.getJSONArray( "data" );
                        }catch( JSONException e ){
                            System.out.print( "transactions Exception " + e.toString() );
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        System.out.println( error );
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = getCustomParams( user.getStudentId(), user.getPassword() );
                if( page > 0 ) params.put( "page", "" + page );
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return getCustomHeaders();
            }
        };
        mRequestQueue.add( request );
    }

    public void balance( final User user ){
        final String URL = Config.API_URL + Config.API_VERSION + "/balance";

        StringRequest request = new StringRequest( Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        try{
                            JSONObject data = new JSONObject( response ).getJSONObject( "balance" ).getJSONObject( "data" );
                            Message.toast( context, "you assfuck got money " + data.getDouble( "credit" ) );
                        }catch( JSONException e ){
                            System.out.println( "Exception balance " + e.toString() );
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        System.out.println( error );
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                return getCustomParams( user.getStudentId(), user.getPassword() );
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return getCustomHeaders();
            }
        };
        mRequestQueue.add( request );
    }

    public User verify( final String studentId, final String password ){
        final String URL = Config.API_URL + Config.API_VERSION + "/verify";
        final User user = new User();
        Log.v( "verify", "" + studentId + " " + password );


        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        try{
                            JSONObject data = new JSONObject( response ).getJSONObject( "verify" ).getJSONObject( "data" );
                            Log.v( "json", response.toString() );
                            if( data.getBoolean( "valid" ) ){
                                user.setStudentId( studentId );
                                user.setPassword( password );
                                Log.v( "Response", user.getStudentId() );
                            }
                        }catch( JSONException e ){
                            System.out.println( "Exception" + e.toString() );
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        // TODO write cool code
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                return getCustomParams( studentId, password );
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                return getCustomHeaders();
            }
        };
        request.setShouldCache( false );
        mRequestQueue.add( request );

        return user;
    }

    private Map<String, String> getCustomHeaders() throws AuthFailureError{
        Map<String, String> headers = new HashMap<>();
        String credentials = Config.API_LOGIN + ":" + Config.API_PASSWORD;
        String auth = "Basic "
                + Base64.encodeToString( credentials.getBytes(),
                Base64.NO_WRAP );
        headers.put( "Authorization", auth );
        return headers;
    }

    private Map<String, String> getCustomParams( String studentID, String password ) throws AuthFailureError{
        Map<String, String> params = new HashMap<String, String>();
        params.put( "studentId", studentID );
        params.put( "password", password );
        return params;
    }

    private void writeTransactions( JSONArray transactions ) throws JSONException, ParseException{
        List<Transaction> list = new ArrayList<>();
        for( int i = 0; i < transactions.length(); i++ ){

            JSONObject obj = transactions.getJSONObject( i );

            DateFormat f = new SimpleDateFormat( "Y-m-d'TH:i:s" );
            Date time = f.parse( obj.getString( "timestamp" ) );

            double amount = obj.getDouble( "amount" );
            String type = obj.getString( "type" );

            JSONObject details = obj.getJSONObject( "details" );
            String title = details.getString( "title" );
            String description = details.getString( "description" );

            JSONObject loc = obj.getJSONObject( "location" );
            String name = loc.getString( "name" );
            double lat = loc.getDouble( "lat" );
            double lng = loc.getDouble( "lng" );

            Transaction t = new Transaction( time, amount, type, title, description, new Location( name, lat, lng ) );
            list.add( t );
        }
    }

}



