package be.krivi.plutus.android.application;

import be.krivi.plutus.android.R;

import java.util.Locale;

/**
 * Created by Krivi on 28/12/15.
 */
public enum Window{
    CREDIT( R.string.credit, "credit", 0 ),
    TRANSACTIONS( R.string.transactions, "transactions", 1 ),
    SETTINGS( R.string.settings, "settings", 2 );

    private int reference;
    private String origin;
    private int id;

    Window( int reference, String origin, int id ){
        this.reference = reference;
        this.origin = origin;
        this.id = id;
    }

    @Override
    public String toString(){
        return "Use toReference() in combination with getText()";
    }

    public int getReference(){
        return reference;
    }

    public String getOrigin(){
        return origin;
    }

    public int getId(){
        return id;
    }
}