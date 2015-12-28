package be.krivi.plutus.android.application;

/**
 * Created by Krivi on 28/12/15.
 */
public enum Language{
    DEFAULT( "", "" ),
    ENGLISH( "English", "en" ),
    DUTCH( "Nederlands", "nl" ),
    FRENCH( "Français", "fr" );

    private String name;
    private String tag;

    Language( String name, String tag ){
        this.name = name;
        this.tag = tag;
    }

    @Override
    public String toString(){
        return name;
    }

    public String toLanguageTag(){
        return tag;
    }
}