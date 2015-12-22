package be.krivi.plutus.android.model;

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

    private void setAmount( double amount ){
        this.amount = amount;
    }

    public String getType(){
        return type;
    }

    private void setType( String type ){
        this.type = type;
    }

    public Date getTimestamp(){
        return timestamp;
    }

    private void setTimestamp( Date timestamp ){
        this.timestamp = timestamp;
    }

    public String getTitle(){
        return title;
    }

    private void setTitle( String title ){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    private void setDescription( String description ){
        this.description = description;
    }

    public Location getLocation(){
        return location;
    }

    private void setLocation( Location location ){
        this.location = location;
    }

    public String toString() {
        //TODO remove me!
        return "Date : " + timestamp.toString() + "\n" +
                "title : " + type + "\n\n";
    }
}
