package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class ConfirmationDialog extends BaseDialog{

    private String message;
    private boolean isReset;

    public static ConfirmationDialog newInstance( String type, String message) {
       return newInstance( type, message, false );
    }

    public static ConfirmationDialog newInstance( String type, String message, boolean isReset) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setDialogType( type );
        dialog.setMessage( message );
        dialog.isReset = isReset;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        if(isReset) {
            setPositiveButton( builder, getString( R.string.reset ) );
            setNegativeButton( builder, getString( R.string.cancel ) );
        } else {
            setPositiveButton( builder, getString( R.string.ok ) );
        }

        setTitle( builder, getType() );
        builder.setMessage( message );

        final AlertDialog dialog = builder.create();

        if(isReset) {
            dialog.setOnShowListener( new DialogInterface.OnShowListener(){
                @Override
                public void onShow( DialogInterface dialogInterface ){
                    dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setTextColor( getResources().getColor( R.color.plutus_red ) );
                }
            } );
        }
        return dialog;
    }


    public void setMessage( String message ){
        this.message = message;
    }


}
