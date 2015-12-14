package be.krivi.plutus.android.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import be.krivi.plutus.android.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity{

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        this.setContentView( R.layout.activity_main );
        ButterKnife.bind( this );
        this.setSupportActionBar( mToolbar );
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
