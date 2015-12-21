package be.krivi.plutus.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.model.Transaction;
import butterknife.Bind;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class TransactionsActivity extends BaseActivity{

    TextView text;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_transactions );

        List<Transaction> transactions = new LinkedList<>();
        try{
            transactions = app.getTransactions();
        }catch( ParseException e ){
            e.printStackTrace();
        }

        text = (TextView) findViewById( R.id.transactionsTEXT );

        String output = "TransactionsActivity bitch : \n";
        for( Transaction t : transactions )
            output += t.toString();

        text.setText( output );

    }

}
