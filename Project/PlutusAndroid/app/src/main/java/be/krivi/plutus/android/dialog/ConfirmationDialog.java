package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class ConfirmationDialog extends BaseDialog{

    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        setPositiveButton( builder, getString( R.string.reset) );
        setNegativeButton( builder, getString( R.string.cancel) );
        setTitle( builder, getType() );
        builder.setMessage( message );

        final AlertDialog dialog = builder.create();

       dialog.setOnShowListener( new DialogInterface.OnShowListener(){
            @Override
            public void onShow( DialogInterface dialogInterface ){
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor( R.color.plutus_red ));
            }
        } );


        return dialog;
    }



}
