package be.krivi.plutus.android.io;

import android.content.Context;
import android.content.SharedPreferences;
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
        return ( sharedPreferences.contains( "studentID" ) && sharedPreferences.contains( "password" ) );
    }

    public void saveCredentials( String studentId, String password ) throws NullPointerException{
        if( studentId == null || password == null || studentId.equals( "" ) || password.equals( "" ) )
            throw new NullPointerException( "Student ID and password cannot be empty" );

        editor.putString( "student_id", studentId );
        editor.putString( "password", password );
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

        return sharedPreferences.getString( "student_id", "" );
    }

}
