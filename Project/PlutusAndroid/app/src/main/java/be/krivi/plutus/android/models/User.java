package be.krivi.plutus.android.models;

/**
 * Created by Jan on 27/11/2015.
 */
public class User{

    private String id;
    private double balance;

    public User( double balance, String id ){
        setId( id );
        setBalance( balance );
    }

    public double getBalance(){
        return balance;
    }

    public void setBalance( double balance ){
        this.balance = balance;
    }

    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }
}
