package be.krivi.plutus.android.io;

import android.content.Context;
import android.content.SharedPreferences;
import be.krivi.plutus.android.model.User;

import java.security.Timestamp;
import java.util.Date;


/**
 * Created by Krivi on 10/12/15.
 */
public class SPAdapter{

    // TODO encrypt/decrypt password rather that saving/reading plain text


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SPAdapter( Context context ){
        sharedPreferences = context.getSharedPreferences( "PlutusPrefs", Context.MODE_PRIVATE );
        editor = sharedPreferences.edit();
    }

    public boolean isUserSaved(){
        return ( sharedPreferences.contains( "student_id" ) && sharedPreferences.contains( "password" ) );
    }

    public void saveCredentials( User user ){
        editor.putString( "student_id", user.getStudentId() );
        editor.putString( "password", user.getPassword() );
        editor.putString( "firstname", user.getFirstname() );
        editor.putString( "lastname", user.getLastname() );
        editor.commit();
    }

    public void saveBalance( double balance ){
        editor.putString( "balance", balance + "" );
        editor.commit();
    }

    public void saveHomeScreen( String homeScreen ){
        editor.putString( "home_screen", homeScreen );
        editor.commit();
    }

    public void savePauseTimestamp(Date timestamp) {
        editor.putLong( "pauseTime", timestamp.getTime() );
    }

    public String getStudentId(){
        return sharedPreferences.getString( "student_id", "" );
    }

    public String getPassword(){
        return sharedPreferences.getString( "password", "" );
    }

    public double getBalance(){
        return Double.parseDouble( sharedPreferences.getString( "balance", "0" ) );
    }

    public String getFirstname(){
        return sharedPreferences.getString( "firstname", "" );
    }

    public String getLastname(){
        return sharedPreferences.getString( "lastname", "" );
    }

    public String getHomeScreen(){
        return sharedPreferences.getString( "home_screen", "" );
    }

    public Date getPauseTimestamp() {
        long milliseconds = sharedPreferences.getLong( "pauseTime", 0 );
        if(milliseconds == 0)
            return null;
        return new Date(milliseconds);
    }


}
