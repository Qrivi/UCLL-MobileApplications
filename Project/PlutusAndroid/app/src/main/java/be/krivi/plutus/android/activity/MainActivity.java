package be.krivi.plutus.android.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.fragment.BalanceFragment;
import be.krivi.plutus.android.fragment.SettingsFragment;
import be.krivi.plutus.android.fragment.TransactionsFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.text.ParseException;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Bind( R.id.wrapperMain )
    DrawerLayout mDrawerLayout;

    @Bind( R.id.wrapperDrawer )
    NavigationView mNavigationView;

    ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        this.setContentView( R.layout.activity_main );
        ButterKnife.bind( this );

        setSupportActionBar( mToolbar );
        setFragment( app.getHomeScreen() );

        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer ){
            public void onDrawerClosed( View view ){
                super.onDrawerClosed( view );
            }

            public void onDrawerOpened( View drawerView ){
                super.onDrawerOpened( drawerView );
            }
        };
        mDrawerLayout.setDrawerListener( mDrawerToggle );
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener( this );


        View headerView = mNavigationView.getHeaderView(0);
        TextView lbl_studentId = (TextView) headerView.findViewById(R.id.lbl_studentId);
        lbl_studentId.setText( app.getCurrentUser().getStudentId() );
        TextView lbl_studentName = (TextView) headerView.findViewById(R.id.lbl_studentName);
        lbl_studentName.setText( app.getCurrentUser().getFirstname() );


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

    @Override
    public void onBackPressed(){
        if( mToolbar.getTitle().toString().equals( app.getHomeScreen() ) )
            finish();
        else
            setFragment( app.getHomeScreen() );
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item ){

        // if user taps active item, no new fragments need to load
        if( !item.isChecked() )
            setFragment( item.getTitle().toString() );

        mDrawerLayout.closeDrawers();
        return true;
    }

    public void setFragment( String fragmentTitle ){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int menuItem = 0;

        switch( fragmentTitle ){
            case "Balance":
                menuItem = R.id.navigation_balance;
                transaction.replace( R.id.wrapperFragment, new BalanceFragment() );
                break;
            case "Transactions":
                menuItem = R.id.navigation_transactions;
                transaction.replace( R.id.wrapperFragment, new TransactionsFragment() );
                break;
            case "Settings":
                menuItem = R.id.navigation_settings;
                transaction.replace( R.id.wrapperFragment, new SettingsFragment() );
                break;
        }

        if( menuItem != 0 )
            mNavigationView.getMenu().findItem( menuItem ).setChecked( true );
        if( getSupportActionBar() != null )
            getSupportActionBar().setTitle( fragmentTitle );

        mToolbar.setTitle( fragmentTitle );
        transaction.commit();
    }
}


