package be.krivi.plutus.android.network.volley;

import android.util.Base64;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.application.PlutusAndroid;
import be.krivi.plutus.android.io.IOService;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.model.User;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jan on 8/12/2015.
 */
public class Client{

    private RequestQueue mRequestQueue;
    private IOService IOService;

    public Client(){
        this.mRequestQueue = VolleySingleton.getINSTANCE().getmRequestQueue();
        IOService = new IOService( PlutusAndroid.getAppContext() );
    }

    public void populateDatabase( final User user, final int page ){
        final String URL = Config.API_URL + Config.API_VERSION + "/transactions";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse( String response ){
                        try{
                            JSONObject data = new JSONObject( response );
                            JSONArray array = data.getJSONArray( "data" );
                            writeTransactions( array );
                        }catch( JSONException e ){
                            //TODO : write exception
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse( VolleyError error ){
                        //TODO : write exception
                        error.printStackTrace();
                    }
                } ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put( "studentId", user.getStudentId() );
                params.put( "password", user.getPassword() );
                if( page > 0 ) params.put( "page", "" + page );
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                String credentials = Config.API_LOGIN + ":" + Config.API_PASSWORD;
                String auth = "Basic "
                        + Base64.encodeToString( credentials.getBytes(),
                        Base64.NO_WRAP );
                headers.put( "Authorization", auth );
                return headers;
            }
        };
        mRequestQueue.add( request );
    }

    private void writeTransactions( JSONArray JSONTransactions ){
        List<Transaction> transactions = new LinkedList<>();
        for( int i = 0; i < JSONTransactions.length(); i++ ){
            JSONObject obj;
            try{
                obj = JSONTransactions.getJSONObject( i );

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                Date time = dateFormat.parse(obj.getString( "timestamp" ));

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
                transactions.add( t );


            }catch( Exception e ){
                //TODO : write exception
                e.printStackTrace();
            }
        }
        IOService.insertListOfTransactions( transactions );
    }
}



