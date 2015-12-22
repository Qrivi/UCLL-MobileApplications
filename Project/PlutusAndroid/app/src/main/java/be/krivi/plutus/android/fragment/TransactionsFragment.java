package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.model.Transaction;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends BaseFragment{


    @Bind( R.id.tmp_transactions )
    TextView tmp_transactions;

    @Bind( R.id.tmp_linearLayout )
    LinearLayout tmp_l;

    public TransactionsFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_transactions, container, false );
        ButterKnife.bind( this, view );
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Fragment Inbox");

        updateView();
        return view;
    }


    public void updateView(){

        Message.toast( app.getApplicationContext(), app.getTransactions().size() + "" );

        for( Transaction t : app.getTransactions() ){
            TextView te = new TextView( app.getApplicationContext() );
            te.setText( t.getTitle() );
            tmp_l.addView( te );
        }
    }


}
