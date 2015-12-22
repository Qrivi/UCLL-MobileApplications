package be.krivi.plutus.android.io;

import android.content.Context;
import android.content.SharedPreferences;
import be.krivi.plutus.android.model.User;

import java.io.IOException;


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

    public boolean isUserRemembered(){
        return ( sharedPreferences.contains( "student_id" ) && sharedPreferences.contains( "password" ) );
    }

    public void saveCredentials( User user ) throws NullPointerException{
        if( user.getStudentId() == null || user.getPassword() == null ||
                user.getStudentId().equals( "" ) || user.getPassword().equals( "" ) )
            throw new NullPointerException( "Student ID and password cannot be empty" );

        editor.putString( "student_id", user.getStudentId() );
        editor.putString( "password", user.getPassword() );
        editor.putString( "firstname", user.getFirstname() );
        editor.putString( "lastname", user.getLastname() );
        editor.commit();
    }

    public void saveBalance( double balance ){
        if( balance < 0 )
            throw new NullPointerException( "Student ID and password cannot be empty" );

        editor.putString( "balance", balance + "" );
        editor.commit();
    }

    public void saveHomeScreen( String homeScreen ) throws NullPointerException{
        if( homeScreen == null || homeScreen.equals( "" ) )
            throw new NullPointerException( "Student ID and password cannot be empty" );

        editor.putString( "home_screen", homeScreen );
        editor.commit();
    }

    public String getStudentId() throws IOException{
        if( !isUserRemembered() )
            throw new IOException( "Student ID is not saved in the preferences file" );

        return sharedPreferences.getString( "student_id", "" );
    }

    public String getPassword() throws IOException{
        if( !isUserRemembered() )
            throw new IOException( "Student ID is not saved in the preferences file" );

        return sharedPreferences.getString( "password", "" );
    }

    public double getBalance() throws IOException{
        if( !isUserRemembered() )
            throw new IOException( "Student ID is not saved in the preferences file" );

        return Double.parseDouble( sharedPreferences.getString( "balance", "" ) );
    }

    public String getFirstname() throws IOException{
        if( !isUserRemembered() )
            throw new IOException( "Student ID is not saved in the preferences file" );

        return sharedPreferences.getString( "firstname", "" );
    }

    public String getLastname() throws IOException{
        if( !isUserRemembered() )
            throw new IOException( "Student ID is not saved in the preferences file" );

        return sharedPreferences.getString( "lastname", "" );
    }

    public String getHomeScreen(){
        return sharedPreferences.getString( "home_screen", null );
    }

}
