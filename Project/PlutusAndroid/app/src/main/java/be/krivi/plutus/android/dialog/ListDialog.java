package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class ListDialog extends BaseDialog{


    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialog = inflater.inflate( R.layout.dialog_list, null );

       // builder.setView( dialog )
          builder.setSingleChoiceItems( options , current, this );
        setNegativeButton( builder, getString( R.string.cancel ) );
        setTitle( builder, getType() );

        return builder.create();
    }

}
