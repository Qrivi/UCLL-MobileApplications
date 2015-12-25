package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreditFragment extends BaseFragment{


    @Bind( R.id.tmp_moneyfield )
    TextView tmp_moneyfield;

    public CreditFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_credit, container, false );
        ButterKnife.bind( this, view );
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Fragment Inbox");

        updateView();
        return view;
    }


    public void updateView(){
        tmp_moneyfield.setText( tmp_moneyfield.getText() + " -> " + app.getCurrentUser().getCredit() );
    }


}
