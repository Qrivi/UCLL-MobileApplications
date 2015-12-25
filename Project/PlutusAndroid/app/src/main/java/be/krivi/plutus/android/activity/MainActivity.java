package be.krivi.plutus.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.fragment.BaseFragment;
import be.krivi.plutus.android.fragment.CreditFragment;
import be.krivi.plutus.android.fragment.SettingsFragment;
import be.krivi.plutus.android.fragment.TransactionsFragment;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.network.volley.VolleyCallback;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.VolleyError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Bind( R.id.wrapperMain )
    public DrawerLayout mDrawerLayout;
    // public for easy access setting SnackBars from elsewhere

    @Bind( R.id.toolbar )
    Toolbar mToolbar;

    @Bind( R.id.wrapperDrawer )
    NavigationView mNavigationView;

    Menu mToolbarMenu;
    ActionBarDrawerToggle mDrawerToggle;

    BaseFragment currentFragment;

    boolean loggingOut;


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

        View headerView = mNavigationView.getHeaderView( 0 );
        TextView lbl_studentId = (TextView)headerView.findViewById( R.id.lbl_studentId );
        lbl_studentId.setText( app.getCurrentUser().getStudentId() );
        TextView lbl_studentName = (TextView)headerView.findViewById( R.id.lbl_studentName );
        lbl_studentName.setText( app.getCurrentUser().getFirstName() + " " + app.getCurrentUser().getLastName() );

        if( app.isNewInstallation() )
            mDrawerLayout.openDrawer( GravityCompat.START );

        if( app.isDatabaseIncomplete() )
            Message.snack( mDrawerLayout, getString( R.string.database_setting_up ) );

        canConnectToInternet();
    }

    @Override
    public void onResume(){
        super.onResume();

        if( app.fetchRequired() ){
            fetchAllData();
            Log.i( "Data status", "outdated -- fetching..." );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ){
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mToolbarMenu = menu;
        getMenuInflater().inflate( R.menu.menu_main, menu );

        SearchView searchView = (SearchView)MenuItemCompat.getActionView( mToolbarMenu.findItem( R.id.menu_search ) );
        searchView.setQueryHint( getString( R.string.find_a_transaction ) );
        searchView.setSubmitButtonEnabled( false );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit( String query ){
                // filter alle transacties ooit
                // TODO niet in versie voor Vogels
                return false;
            }
            @Override
            public boolean onQueryTextChange( String text ){
                if( currentFragment instanceof TransactionsFragment){
                    TransactionsFragment fragment = ( TransactionsFragment)currentFragment;
                    fragment.filterTransactions( text );
                }
                return false;
            }
        } );
        searchView.setOnCloseListener( new SearchView.OnCloseListener(){
            @Override
            public boolean onClose(){
                updateFragment();

                Log.v( "SIZE OF LE LIST", " o lal a "  );
                return false;
            }
        } );

        setMenuOptions( mToolbar.getTitle().toString() );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //if( item.getTitle().toString().equals( getString( R.string.search ) ) )


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
        if( item.getItemId() == R.id.navigation_signout ){
            logout();
            return true;
        }else if( !item.isChecked() ){
            setFragment( item.getTitle().toString() );
        }
        mDrawerLayout.closeDrawers();
        return true;
    }

    public void setFragment( String fragmentTitle ){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction switcher = manager.beginTransaction();
        int nav = 0;

        switch( fragmentTitle ){
            case "Credit":
                nav = R.id.navigation_credit;
                currentFragment = new CreditFragment();
                break;
            case "Transactions":
                nav = R.id.navigation_transactions;
                currentFragment = new TransactionsFragment();
                break;
            case "Settings":
                nav = R.id.navigation_settings;
                currentFragment = new SettingsFragment();
                break;
        }

        setMenuOptions( fragmentTitle );
        switcher.replace( R.id.wrapperFragment, currentFragment );

        if( nav != 0 )
            mNavigationView.getMenu().findItem( nav ).setChecked( true );
        if( getSupportActionBar() != null )
            getSupportActionBar().setTitle( fragmentTitle );

        mToolbar.setTitle( fragmentTitle );
        switcher.commit();
    }

    private void setMenuOptions( String fragmentTitle ){

        if( mToolbarMenu != null ){
            MenuItem search = mToolbarMenu.findItem( R.id.menu_filter );
            MenuItem filter = mToolbarMenu.findItem( R.id.menu_search );

            switch( fragmentTitle ){
                case "Transactions":
                    search.setVisible( true );
                    filter.setVisible( true );
                    break;
                default:
                    search.setVisible( false );
                    filter.setVisible( false );
                    break;
            }
        }
    }

    public void fetchAllData(){
        if( canConnectToInternet() ){
            fetchCreditData();
            fetchTransactionsData();
        }
    }

    public void fetchCreditData(){

        if( canConnectToInternet() ){
            Map<String, String> params = new HashMap<>();
            params.put( "studentId", app.getCurrentUser().getStudentId() );
            params.put( "password", app.getCurrentUser().getPassword() );

            app.contactAPI( params, Config.API_ENDPOINT_CREDIT, new VolleyCallback(){
                @Override
                public void onSuccess( String response ){
                    try{
                        JSONObject meta = new JSONObject( response ).getJSONObject( "meta" );
                        String fetchDate = meta.getString( "timestamp" );

                        JSONObject data = new JSONObject( response ).getJSONObject( "data" );
                        double credit = data.getDouble( "amount" );

                        app.writeUserCredit( credit, fetchDate );
                        app.loadData();
                        updateFragment();
                    }catch( JSONException e ){
                        Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_fetching_credit ) + e.getMessage() );
                    }
                }

                @Override
                public void onFailure( VolleyError error ){
                    Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_endpoint_credit ) );
                }
            } );
        }
    }

    public void fetchTransactionsData(){

        if( canConnectToInternet() ){
            Map<String, String> params = new HashMap<>();
            params.put( "studentId", app.getCurrentUser().getStudentId() );
            params.put( "password", app.getCurrentUser().getPassword() );
            params.put( "page", "1" );

            app.contactAPI( params, Config.API_ENDPOINT_TRANSACTIONS, new VolleyCallback(){
                @Override
                public void onSuccess( String response ){
                    try{
                        JSONArray array = new JSONObject( response ).getJSONArray( "data" );
                        app.writeTransactions( array );
                        app.completeDatabase( 2 );
                        updateFragment();
                    }catch( JSONException e ){
                        Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_fetching_transactions ) + e.getMessage() );
                    }
                }

                @Override
                public void onFailure( VolleyError error ){
                    Message.obtrusive( app.getCurrentActivity(), getString( R.string.error_endpoint_transactions ) );
                }
            } );
        }
    }

    private void updateFragment(){

            switch( mToolbar.getTitle().toString() ){
                case "Credit":
                    CreditFragment bf = (CreditFragment)currentFragment;
                    bf.updateView();
                    break;
                case "Transactions":
                    TransactionsFragment tf = (TransactionsFragment)currentFragment;
                    tf.updateView();
                    break;
            }
    }

    private boolean canConnectToInternet(){

        if( !app.isNetworkAvailable() ){
            Message.snack( mDrawerLayout, getString( R.string.no_internet_connection ) );
            return false;
        }
        return true;

    }

    private void logout(){
        loggingOut = true;
        app.logoutUser();
        startActivity( new Intent( app.getApplicationContext(), LoginActivity.class ) );
        finish();
    }

}


