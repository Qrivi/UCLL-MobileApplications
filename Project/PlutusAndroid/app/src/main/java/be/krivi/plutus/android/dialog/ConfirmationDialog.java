package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class ConfirmationDialog extends BaseDialog{

    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );

        setPositiveButton( builder, getString( R.string.ok) );
        setNegativeButton( builder, getString( R.string.cancel) );
        setTitle( builder, getType() );

        return builder.create();
    }



}
