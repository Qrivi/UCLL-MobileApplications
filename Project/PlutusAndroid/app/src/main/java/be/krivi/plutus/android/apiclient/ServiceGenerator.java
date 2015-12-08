package be.krivi.plutus.android.apiclient;

import android.util.Base64;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by Krivi on 08/12/15.
 */
public class ServiceGenerator{

    public static final String API_BASE_URL = "http://labs.krivi.be/PlutusAPI/v1";
    public static final String API_USER = "Plutus";
    public static final String API_PASS = "6298e5dbc7c0475c2273a8e2371695d4756b8f45";

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl( API_BASE_URL )
                    .addConverterFactory( GsonConverterFactory.create() );

    public static <S> S createService( Class<S> serviceClass ){
        return createService( serviceClass, API_USER, API_PASS );
    }

    public static <S> S createService( Class<S> serviceClass, String username, String password ){
        if( username != null && password != null ){
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString( credentials.getBytes(), Base64.NO_WRAP );

            httpClient.interceptors().clear();
            httpClient.interceptors().add( new Interceptor(){
                @Override
                public Response intercept( Interceptor.Chain chain ) throws IOException{
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header( "Authorization", basic )
                            .header( "Accept", "applicaton/json" )
                            .method( original.method(), original.body() );

                    Request request = requestBuilder.build();
                    return chain.proceed( request );
                }
            } );
        }

        Retrofit retrofit = builder.client( httpClient ).build();
        return retrofit.create( serviceClass );
    }
}