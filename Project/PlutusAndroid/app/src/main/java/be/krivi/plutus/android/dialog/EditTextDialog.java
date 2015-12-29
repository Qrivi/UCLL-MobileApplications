package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class EditTextDialog extends BaseDialog{

    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialog = inflater.inflate( R.layout.dialog_edittext, null );

        builder.setView( dialog );
        setTitle( builder, getType() );
        builder.setMessage( message );
        setPositiveButton( builder, getString( R.string.ok) );
        setNegativeButton( builder, getString( R.string.cancel) );

        return builder.create();
    }


}
