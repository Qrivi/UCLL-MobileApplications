package be.krivi.plutus.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BalanceActivity extends BaseActivity{

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Bind( R.id.textView )
    TextView mText;


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        this.setContentView( R.layout.activity_balance );
        ButterKnife.bind( this );
        this.setSupportActionBar( mToolbar );

        mText.setText( "U coolboy got monies : " + app.getCurrentUser().getBalance() );
    }

    @OnClick( R.id.buttonVOORUIT )
    public void infoClickHandler() {
        startActivity( new Intent( app.getApplicationContext(), TransactionsActivity.class ) );
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_settings ){
            return true;
        }

        return super.onOptionsItemSelected( item );
    }
}
