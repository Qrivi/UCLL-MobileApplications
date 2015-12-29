package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class EditTextDialog extends BaseDialog{

    private String message;

    public static EditTextDialog newInstance(String type, String message) {
        EditTextDialog dialog = new EditTextDialog();
        dialog.setDialogType( type );
        dialog.setMessage( message );
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ){
        super.onCreateDialog( savedInstanceState );

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

    public void setMessage( String message ){
        this.message = message;
    }


}
