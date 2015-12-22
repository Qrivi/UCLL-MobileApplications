package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.network.volley.VolleyCallback;
import be.krivi.plutus.android.view.Message;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.android.volley.VolleyError;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends BaseFragment{


    @Bind( R.id.tmp_moneyfield )
    TextView tmp_moneyfield;

    public BalanceFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_balance, container, false);
        ButterKnife.bind( this , view );
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Fragment Inbox");

        updateView();
        return view;
    }


    private void updateView() {
        tmp_moneyfield.setText( tmp_moneyfield.getText() + " -> " + app.getCurrentUser().getBalance() );
    }

    public void fetchBalance(){

        Map<String, String> params = new HashMap<>();
        params.put( "studentId", app.getCurrentUser().getStudentId() );
        params.put( "password", app.getCurrentUser().getPassword() );

        app.contactAPI( params, Config.API_BALANCE, new VolleyCallback(){
            @Override
            public void onSuccess( String response ){
                try{
                    JSONObject data = new JSONObject( response ).getJSONObject( "data" );
                    double balance = data.getDouble( "credit" );
                    app.initializeUserBalance( balance );
                    updateView();
                }catch( JSONException e ){
                    Message.obtrusive( app.getCurrentActivity(), "Error fetching balance: \n" + e.getMessage() );
                }
            }

            @Override
            public void onFailure( VolleyError error ){
                Message.obtrusive( app.getCurrentActivity(), error.getMessage() );
            }
        } );
    }


}
