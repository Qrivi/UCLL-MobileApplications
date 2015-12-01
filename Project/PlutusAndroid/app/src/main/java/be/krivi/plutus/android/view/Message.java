package be.krivi.plutus.android.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jan on 30/11/2015.
 */
public class Message{

    public  static void message( Context context, String message) {
        Toast.makeText( context, message, Toast.LENGTH_LONG).show();
    }
}
