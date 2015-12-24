package be.krivi.plutus.android.application;

/**
 * Created by Krivi on 13/12/15.
 */
public abstract class Config{

    public static String PROJECT_URL = "https://qrivi.github.io/Plutus";

    public static String API_URL = "http://labs.krivi.be/plutus/api"; // will of course be https in the future!
    public static String API_VERSION = "/v1";

    public static String API_LOGIN = "Plutus";
    public static String API_PASSWORD = "6298e5dbc7c0475c2273a8e2371695d4756b8f45";

    public static String API_ENPOINT_VERIFY = "/verify";
    public static String API_ENDPOINT_BALANCE = "/balance";
    public static String API_ENDPOINT_TRANSACTIONS = "/transactions";

    public static String API_DEFAULT_CURRENCY_SYMBOL = "€";

    //TODO change me to 60 minutes or something!
    public static int APP_DEFAULT_SNOOZE_TIME = 2;
    public static int APP_DEFAULT_LIST_SIZE = 25;

    public static boolean SETTINGS_DEFAULT_BALANCE_REPRESENTATION = true;
    public static double SETTINGS_DEFAULT_BALANCE_REPRESENTATION_MIN = 10;
    public static double SETTINGS_DEFAULT_BALANCE_REPRESENTATION_MAX = 50;
    public static boolean SETTINGS_DEFAULT_NOTIFICATIONS = true;
    public static String SETTINGS_DEFAULT_HOMESCREEN = "Balance";

}
