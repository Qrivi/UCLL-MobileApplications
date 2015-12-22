package be.krivi.plutus.android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jan on 30/11/2015.
 */
public abstract class Message{

    public static void toast( Context context, String message ){
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
    }

    public static void snack( View view, String message ){
        Snackbar.make( view, message, Snackbar.LENGTH_LONG ).show();
    }

    public static void obtrusive( Context context, String message ){
        new AlertDialog.Builder( context )
                .setTitle( "Oops, something went wrong!" )
                .setMessage( message )
                .setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick( DialogInterface dialog, int which ){
                        // empty dismisses dialog
                    }
                } )
                .show();
    }


}
