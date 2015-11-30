package be.krivi.plutus.android.models;

import java.util.Date;


/**
 * Created by Jan on 27/11/2015.
 */
public class Transaction{

    private Date timestamp;
    private double amount;
    private String type;

    private String title, description;
    private Location location;

    public Transaction( Date timestamp,  double amount, String type,  String title, String description, Location location ){
        setTimestamp( timestamp );
        setAmount( amount );
        setType( type );
        setTitle( title );
        setDescription( description );
        setLocation( location );
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount( double amount ){
        this.amount = amount;
    }

    public String getType(){
        return type;
    }

    public void setType( String type ){
        this.type = type;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    public void setTimestamp( Date timestamp ){
        this.timestamp = timestamp;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle( String title ){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription( String description ){
        this.description = description;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation( Location location ){
        this.location = location;
    }
}
