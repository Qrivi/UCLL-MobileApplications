package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        set = 0;
        MainActivity main = (MainActivity)getActivity();
        main.fetchTransactionsData();
    }

    public void updateView(){
        transactions = app.getTransactionsSet( set );
        adapter.setRowData( transactions );
        //adapter.notifyDataSetChanged();

        mSwipeRefresh.setRefreshing( false );

        //TODO send this line to Africa
        Message.toast( app.getApplicationContext(), transactions.size() + " loads" );
    }
}
