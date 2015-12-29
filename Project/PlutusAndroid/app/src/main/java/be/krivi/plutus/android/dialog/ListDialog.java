package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class ListDialog extends BaseDialog{


    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        builder.setSingleChoiceItems( options, current, this );
        setNegativeButton( builder, getString( R.string.cancel ) );
        setTitle( builder, getType() );

        return builder.create();
    }

}
