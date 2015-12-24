package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import be.krivi.plutus.android.view.TransactionAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind( R.id.swipeRefreshTransactions )
    SwipeRefreshLayout mSwipeRefresh;

    @Bind( R.id.recyclerTransactions )
    RecyclerView mRecycler;

    TransactionAdapter adapter;
    List<Transaction> transactions;
    private int set;
    boolean loading = true;

    public TransactionsFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        final View view = inflater.inflate( R.layout.fragment_transactions, container, false );
        ButterKnife.bind( this, view );

        mSwipeRefresh.setOnRefreshListener( this );

        set = 0;
        transactions = new LinkedList<>();
        adapter = new TransactionAdapter( getActivity(), transactions );

        mRecycler.setAdapter( adapter );
        mRecycler.setLayoutManager( new LinearLayoutManager( getActivity() ) );

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)mRecycler.getLayoutManager();

        mRecycler.addOnScrollListener( new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy ){
                super.onScrolled( recyclerView, dx, dy );

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if( loading ){
                    if( ( visibleItemCount + pastVisiblesItems ) >= totalItemCount ){
                        //TODO maak mooiere code!
                        loading = false;
                        set++;

                        List<Transaction> updatedTransactions = new LinkedList<Transaction>( );
                        updatedTransactions.addAll(transactions);
                        updatedTransactions.addAll( app.getTransactionsSet( set ));
                        transactions = updatedTransactions;

                        adapter.setTransactions( transactions );
                        adapter.notifyDataSetChanged();

                        //TODO REMOVE ME!
                        Message.toast( app.getApplicationContext(), transactions.size() + "" );
                    }
                }
            }
        } );
        updateView();
        return view;
    }

    public void updateView(){

        transactions = app.getTransactionsSet( set );
        adapter.setTransactions( transactions );
        adapter.notifyDataSetChanged();

        //TODO send this line to Africa
        Message.toast( app.getApplicationContext(), transactions.size() + "" );
    }

    @Override
    public void onRefresh(){
        //TODO write cool code >:D

        Message.toast( app.getApplicationContext(), "dank maymays xD" );
    }
}
