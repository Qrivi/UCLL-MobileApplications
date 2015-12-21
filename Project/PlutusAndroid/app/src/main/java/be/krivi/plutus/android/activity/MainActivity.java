package be.krivi.plutus.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Bind( R.id.textView )
    TextView mText;

    @Bind( R.id.navigation_view )
    NavigationView mNavigationView;

    @Bind( R.id.wrapperMain )
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        this.setContentView( R.layout.activity_main );
        ButterKnife.bind( this );

        this.setSupportActionBar( mToolbar );

        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer ){

            public void onDrawerClosed( View view ){
                super.onDrawerClosed( view );
            }

            public void onDrawerOpened( View drawerView ){
                super.onDrawerOpened( drawerView );
            }
        };

        mDrawerLayout.setDrawerListener( mDrawerToggle );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeButtonEnabled( true );
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected( MenuItem item ){
                item.setChecked( true );
                startActivity( new Intent( app.getApplicationContext(), TransactionsActivity.class ) );

                overridePendingTransition(0,0);

                mDrawerLayout.closeDrawers();
                return true;
            }
        } );

        mText.setText( "U coolboy got monies : " + app.getCurrentUser().getBalance() );

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
