package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends BaseFragment{


    @Bind( R.id.tmp_transactions )
    TextView tmp_transactions;

    public TransactionsFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_transactions, container, false );
        ButterKnife.bind( this, view );
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Fragment Inbox");

        fetchTransactions();
        tmp_transactions.setText( app.getTransactions().toString() );


        return view;
    }

    private void updateView(){

        for( Transaction t : app.getTransactions() )
            tmp_transactions.setText( tmp_transactions.getText() + t.toString() );
    }

    public void fetchTransactions(){

        Map<String, String> params = new HashMap<>();
        params.put( "studentId", app.getCurrentUser().getStudentId() );
        params.put( "password", app.getCurrentUser().getPassword() );
        params.put( "page", 0 + "" );

        app.contactAPI( params, Config.API_TRANSACTIONS, new VolleyCallback(){
            @Override
            public void onSuccess( String response ){
                try{
                    JSONArray array = new JSONObject( response ).getJSONArray( "data" );
                    app.writeTransactions( array );
                    updateView();
                }catch( JSONException e ){
                    Message.obtrusive( app.getCurrentActivity(), "Error fetching transactions: \n" + e.getMessage() );
                }
            }

            @Override
            public void onFailure( VolleyError error ){
                Message.obtrusive( app.getCurrentActivity(), error.getMessage() );
            }
        } );

    }


}
