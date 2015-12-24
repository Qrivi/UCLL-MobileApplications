package be.krivi.plutus.android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 30/11/2015.
 */
public abstract class Message{

    public static void toast( Context context, String message ){
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
    }

    public static void snack( View view, String message ){
        Snackbar.make( view, message, Snackbar.LENGTH_LONG )
                .setActionTextColor( ContextCompat.getColor( view.getContext(), R.color.ucll_light_blue ) )
                .setAction( R.string.dismiss, new View.OnClickListener(){
                    @Override
                    public void onClick( View v ){
                        // empty dismisses snackbar
                    }
                } )
                .show();
    }

    public static void obtrusive( Context context, String message ){
        new AlertDialog.Builder( context )
                .setTitle( context.getString( R.string.something_went_wrong) )
                .setMessage( message )
                .setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick( DialogInterface dialog, int which ){
                        // empty dismisses dialog
                    }
                } )
                .show();
    }


}
