package be.krivi.plutus.android.io;

import android.content.Context;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;

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

    public boolean isUserRemembered() {
        return spAdapter.isUserRemembered();
    }

    public void saveCredentials(String studentId, String password) throws NullPointerException {
        spAdapter.saveCredentials(studentId, password);
    }

    public long insertLocation( Location l ){
        return dbAdapter.insertLocation( l );
    }

    public long insertTransaction( Transaction t ){
        return dbAdapter.insertTransaction( t );
    }

    public List<Transaction> getAllTransactions() throws ParseException{
        return dbAdapter.getAllTransactions();
    }

    public Transaction getTransaction( Date timestamp ) throws ParseException{
        return dbAdapter.getTransaction( timestamp );
    }


}
