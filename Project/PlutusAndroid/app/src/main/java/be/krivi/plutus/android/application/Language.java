package be.krivi.plutus.android.application;

import java.util.Locale;

/**
 * Created by Krivi on 28/12/15.
 */
public enum Language{
    DEFAULT( Locale.getDefault(), "", "", 0 ),
    ENGLISH( Locale.forLanguageTag( "en" ), "English" , "en", 1 ),
    DUTCH( Locale.forLanguageTag( "nl" ), "Nederlands", "nl", 2 ),
    FRENCH( Locale.forLanguageTag( "fr" ), "Français", "fr", 3 );

    private Locale locale;
    private String name;
    private String tag;
    private int id;

    Language( Locale locale, String name, String tag, int id ){
        this.locale = locale;
        this.name = name;
        this.tag = tag;
        this.id = id;
    }

    public Locale toLocale(){
        return locale;
    }

    @Override
    public String toString(){
        return name;
    }

    public String toLanguageTag(){
        return tag;
    }

    public int getId() {
        return id;
    }
}