package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import be.krivi.plutus.android.view.TransactionsAdapter;
import be.krivi.plutus.android.view.TransactionsOnScrollListener;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind( R.id.swipeRefreshTransactions )
    SwipeRefreshLayout mSwipeRefresh;

    @Bind( R.id.recyclerTransactions )
    RecyclerView mRecycler;

    private MainActivity main;
    private TransactionsAdapter adapter;
    private List<Transaction> transactions;
    private int set;

    public TransactionsFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_transactions, container, false );
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );
        ButterKnife.bind( this, view );

        main = (MainActivity) getActivity();

        set = 0;
        transactions = new LinkedList<>();
        adapter = new TransactionsAdapter( getActivity(), transactions );

        mRecycler.setAdapter( adapter );
        mRecycler.setLayoutManager( linearLayoutManager );

        mSwipeRefresh.setOnRefreshListener( this );
        mSwipeRefresh.setColorSchemeResources(
                R.color.ucll_dark_blue,
                R.color.ucll_light_blue,
                R.color.ucll_pink );
        mRecycler.addOnScrollListener( new TransactionsOnScrollListener( linearLayoutManager ){
            @Override
            public void onLoadMore( int current_set ){
                // TODO implement
                Message.toast( getContext(), "loading set " + current_set );
            }
        } );

        updateView();
        return view;
    }

    @Override
    public void onRefresh(){
        MainActivity main = (MainActivity)getActivity();
        if( app.isNetworkAvailable() ){
            set = 0;
            main.fetchTransactionsData();
        }else{
            Message.snack( main.mDrawerLayout, getString( R.string.no_internet_connection ) );
            mSwipeRefresh.setRefreshing( false );
        }
    }

    public void updateView(){
        transactions = app.getTransactionsSet( set );
        adapter.setRowData( transactions );

        main.mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED );
        mSwipeRefresh.setEnabled( true );
        mSwipeRefresh.setRefreshing( false );

        //TODO send this line to Africa
        Message.toast( app.getApplicationContext(), transactions.size() + " loads" );
    }

    public void filterTransactions( String filter ){
        List<Transaction> filteredList = new LinkedList<>();
        CharSequence f = filter.toLowerCase();

        for( Transaction t : transactions )
            if( t.getTitle().toLowerCase().contains( f ) )
                filteredList.add( t );

        main.mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
        mSwipeRefresh.setEnabled( false );
        adapter.setRowData( filteredList );
    }
}
