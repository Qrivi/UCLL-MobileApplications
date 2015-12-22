package be.krivi.plutus.android.io;

import android.content.Context;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jan on 10/12/2015.
 */
public class IOService{

    private DBAdapter dbAdapter;
    private SPAdapter spAdapter;

    public IOService( Context context ){
        this.dbAdapter = new DBAdapter( context );
        this.spAdapter = new SPAdapter( context );
    }

    public boolean isUserRemembered(){
        return spAdapter.isUserRemembered();
    }

    public void saveCredentials( User user ) throws NullPointerException{
        spAdapter.saveCredentials( user );
    }

    public void saveBalance(double balance)  throws NullPointerException {
        spAdapter.saveBalance(balance);
    }

    public void saveHomeScreen(String homeScreen) throws NullPointerException {
        spAdapter.saveHomeScreen( homeScreen );
    }

    public String getStudentId() throws IOException{
        return spAdapter.getStudentId();
    }

    public String getPassword() throws IOException{
        return spAdapter.getPassword();
    }

    public double getBalance() throws IOException{
        return spAdapter.getBalance();
    }

    public String getFirstname() throws IOException{
        return spAdapter.getFirstname();
    }

    public String getLastname() throws IOException{
        return spAdapter.getLastname();
    }

    public String getHomeScreen() {
        return spAdapter.getHomeScreen();
    }



    public void writeTransactions( JSONArray JSONTransactions ){

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
                insertTransaction( t );

            }catch( Exception e ){
                //TODO : write exception
                e.printStackTrace();
            }
        }
    }

    public long insertTransaction( Transaction t ){
        dbAdapter.insertLocation( t.getLocation() );
        return dbAdapter.insertTransaction( t ) ;
    }

    public List<Transaction> getAllTransactions() throws ParseException{
        return dbAdapter.getAllTransactions();
    }

    public Transaction getTransaction( Date timestamp ) throws ParseException{
        return dbAdapter.getTransaction( timestamp );
    }


}
