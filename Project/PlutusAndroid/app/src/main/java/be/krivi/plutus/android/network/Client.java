package be.krivi.plutus.android.network;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import be.krivi.plutus.android.view.Message;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jan on 8/12/2015.
 */
public class Client{


    private RequestQueue mRequestQueue;
    private Context context;
    private final String USERNAME = "Plutus";
    private final String PASSWORD = "6298e5dbc7c0475c2273a8e2371695d4756b8f45";

    public Client( Context c ){
        mRequestQueue = VolleySingleton.getINSTANCE().getmRequestQueue();
        context = c;
    }

    public void send(){

        Uri.Builder uri = new Uri.Builder();
        uri.scheme( "http" );
        uri.authority( "labs.krivi.be" );
        uri.path( "PlutusAPI/v1/verify" );
        String url = uri.build().toString();


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.toast( context, "Failed to achieved something in this life!" );
                        System.out.println( "No no no you suk it" );
                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<String, String>(){
                };
                // add headers <key,value>
                String credentials = USERNAME + ":" + PASSWORD;
                String auth = "Basic "
                        + Base64.encodeToString( credentials.getBytes(),
                        Base64.NO_WRAP );
                headers.put( "Authorization", auth );

                return headers;
            }

            @Override
            public byte[] getBody()  {

                try
                {
                    final String body = "?studentid=" + "r0123456" + // assumes username is final and is url encoded.
                            "&password=" + "Pass1234" ;// assumes password is final and is url encoded.
                    return body.getBytes("utf-8");
                }
                catch (Exception ex) { }

                return null;

            }
        };

        mRequestQueue.add( request );
    }

}



