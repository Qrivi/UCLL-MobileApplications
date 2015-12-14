package be.krivi.plutus.android.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import be.krivi.plutus.android.model.Location;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DBAdapter{

    DBHelper helper;

    public DBAdapter( Context context ){
        context = context;
        helper = new DBHelper( context );
    }

    public long insertLocation( Location l ){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( DBHelper.LOCATIONS_NAME, l.getName() );
        values.put( DBHelper.LOCATIONS_LNG, l.getLng() );
        values.put( DBHelper.LOCATIONS_LAT, l.getLat() );
        return db.insert( DBHelper.LOCATIONS_TABLE_NAME, null, values );
    }

    public long insertTransaction( Transaction t ){
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        DateFormat f = new SimpleDateFormat( "Y-m-d'TH:i:s" );
        values.put( DBHelper.TRANSACTIONS_TIMESTAMP, f.format( t.getTimestamp() ) );
        values.put( DBHelper.TRANSACTIONS_AMOUNT, t.getAmount() );
        values.put( DBHelper.TRANSACTIONS_TYPE, t.getType() );
        values.put( DBHelper.TRANSACTIONS_TITLE, t.getType() );
        values.put( DBHelper.TRANSACTIONS_DESCRIPTION, t.getDescription() );
        values.put( DBHelper.TRANSACTIONS_LOCATION, t.getLocation().getName() );
        return db.insert( DBHelper.TRANSACTIONS_TABLE_NAME, null, values );

    }

    public List<Transaction> getAllTransactions() throws ParseException{

        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DBHelper.TRANSACTIONS_TIMESTAMP, DBHelper.TRANSACTIONS_AMOUNT, DBHelper.TRANSACTIONS_TYPE,
                DBHelper.TRANSACTIONS_TITLE, DBHelper.TRANSACTIONS_TITLE, DBHelper.TRANSACTIONS_DESCRIPTION,
                DBHelper.LOCATIONS_NAME};
        Cursor cursor = db.query(
                DBHelper.TRANSACTIONS_TABLE_NAME, columns, null, null, null, null, DBHelper.TRANSACTIONS_TIMESTAMP );

        List<Transaction> transactions = new LinkedList();
        DateFormat f = new SimpleDateFormat( "Y-m-d'TH:i:s" );

        while( cursor.moveToNext() ){
            Date timestamp = f.parse( cursor.getString( 1 ) );
            double amount = cursor.getDouble( 2 );
            String type = cursor.getString( 3 );
            String title = cursor.getString( 4 );
            String description = cursor.getString( 5 );
            String location = cursor.getString( 6 );

            Transaction t = new Transaction( timestamp, amount, type, title, description, new Location( location, 0, 0 ) );
            transactions.add( t );
        }

        return transactions;
    }

    public Transaction getTransaction( Date timestamp ) throws ParseException{

        SQLiteDatabase db = helper.getWritableDatabase();
        DateFormat f = new SimpleDateFormat( "Y-m-d'TH:i:s" );

        String[] columns = {DBHelper.TRANSACTIONS_TIMESTAMP, DBHelper.TRANSACTIONS_AMOUNT, DBHelper.TRANSACTIONS_TYPE,
                DBHelper.TRANSACTIONS_TITLE, DBHelper.TRANSACTIONS_TITLE, DBHelper.TRANSACTIONS_DESCRIPTION,
                DBHelper.LOCATIONS_NAME, DBHelper.LOCATIONS_LNG, DBHelper.LOCATIONS_LAT};
        Cursor cursor = db.query(
                DBHelper.TRANSACTIONS_TABLE_NAME + "INNER JOIN" + DBHelper.LOCATIONS_TABLE_NAME +
                        "USING(" + DBHelper.LOCATIONS_NAME + ")",
                columns,
                DBHelper.TRANSACTIONS_TIMESTAMP + "= '" + f.format( timestamp ) + "'",
                null, null, null, DBHelper.TRANSACTIONS_TIMESTAMP );

        DateFormat format = new SimpleDateFormat( "Y-m-d'TH:i:s" );
        cursor.moveToNext();

        Date t = format.parse( cursor.getString( 1 ) );
        double amount = cursor.getDouble( 2 );
        String type = cursor.getString( 3 );
        String title = cursor.getString( 4 );
        String description = cursor.getString( 5 );
        String location = cursor.getString( 6 );
        double lng = cursor.getDouble( 7 );
        double lat = cursor.getDouble( 8 );

        return new Transaction( t, amount, type, title, description, new Location( location, lng, lat ) );
    }

    static class DBHelper extends SQLiteOpenHelper{


        private static final int DB_VERSION = 1;
        private static final String DB_NAME = "PlutusDB";
        private Context context;

        private static final String TRANSACTIONS_TABLE_NAME = "TRANSACTIONS";
        private static final String TRANSACTIONS_TIMESTAMP = "_Timestamp";
        private static final String TRANSACTIONS_AMOUNT = "Amount";
        private static final String TRANSACTIONS_TYPE = "Type";
        private static final String TRANSACTIONS_TITLE = "Amount";
        private static final String TRANSACTIONS_DESCRIPTION = "Description";
        private static final String TRANSACTIONS_LOCATION = "Location";

        private static final String LOCATIONS_TABLE_NAME = "Locations";
        private static final String LOCATIONS_NAME = "Name";
        private static final String LOCATIONS_LNG = "Lng";
        private static final String LOCATIONS_LAT = "Lat";


        public DBHelper( Context context ){
            super( context, DB_NAME, null, DB_VERSION );
            this.context = context;
        }

        @Override
        public void onCreate( SQLiteDatabase db ){
            String query = "CREATE TABLE" + LOCATIONS_TABLE_NAME + "(" +
                    LOCATIONS_NAME + "VARCHAR(25) PRIMARY KEY NOT NULL," +
                    LOCATIONS_LNG + "DOUBLE NOT NULL," +
                    LOCATIONS_LAT + "DOUBLE NOT NULL);" +

                    "CREATE TABLE" + TRANSACTIONS_TABLE_NAME + "(" +
                    TRANSACTIONS_TIMESTAMP + " TIMESTAMP PRIMARY KEY NOT NULL," +
                    TRANSACTIONS_AMOUNT + " DOUBLE NOT NULL," +
                    TRANSACTIONS_TYPE + " VARCHAR(7) NOT NULL," +
                    TRANSACTIONS_TITLE + " VARCHAR(25) NOT NULL," +
                    TRANSACTIONS_DESCRIPTION + "VARCHAR(255) NOT NULL," +
                    TRANSACTIONS_LOCATION + "VARCHAR(25) NOT NULL" +
                    "FOREIGN KEY(" + TRANSACTIONS_LOCATION + ") REFERENCES " + LOCATIONS_TABLE_NAME + "(" + LOCATIONS_NAME + "))";
            try{
                db.execSQL( query );
            }catch( SQLException e ){
                Message.toast( context, "Something went wrong in onCreate!" );
            }

        }

        @Override
        public void onUpgrade( SQLiteDatabase db, int i, int i1 ){

            try{
                db.execSQL( "DROP TABLE IF EXIST" + TRANSACTIONS_LOCATION );
                db.execSQL( "DROP TABLE IF EXIST" + LOCATIONS_TABLE_NAME );
                onCreate( db );
            }catch( SQLException e ){
                Message.toast( context, "Something went wrong mate in onUpgrade!" );
            }
        }
    }
}
