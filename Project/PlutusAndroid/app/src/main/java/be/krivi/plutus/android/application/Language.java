package be.krivi.plutus.android.application;

import java.util.Locale;

/**
 * Created by Krivi on 28/12/15.
 */
public enum Language{
    DEFAULT( Locale.getDefault(), "", "" ),
    ENGLISH( Locale.forLanguageTag( "en" ), "English" , "en" ),
    DUTCH( Locale.forLanguageTag( "nl" ), "Nederlands", "nl" ),
    FRENCH( Locale.forLanguageTag( "fr" ), "Français", "fr" );

    private Locale locale;
    private String name;
    private String tag;

    Language( Locale locale, String name, String tag ){
        this.locale = locale;
        this.name = name;
        this.tag = tag;
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
}