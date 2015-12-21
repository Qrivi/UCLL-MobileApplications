package be.krivi.plutus.android.io;

import android.content.Context;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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


    public long insertLocation( Location l ){
        return dbAdapter.insertLocation( l );
    }

    public long insertTransaction( Transaction t ){
        return dbAdapter.insertTransaction( t );
    }

    public void insertListOfTransactions(List<Transaction> transactions) {
        for(Transaction t : transactions) {
            insertLocation(t.getLocation());
            insertTransaction( t );
        }
    }


    public List<Transaction> getAllTransactions() throws ParseException{
        return dbAdapter.getAllTransactions();
    }

    public Transaction getTransaction( Date timestamp ) throws ParseException{
        return dbAdapter.getTransaction( timestamp );
    }


}
