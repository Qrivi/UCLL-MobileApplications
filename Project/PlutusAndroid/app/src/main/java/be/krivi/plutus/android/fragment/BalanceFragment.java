package be.krivi.plutus.android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.krivi.plutus.android.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceFragment extends Fragment{

    public BalanceFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate( R.layout.fragment_balance, container, false);

        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Fragment Inbox");

        TextView t = (TextView) view.findViewById( R.id.tmp_moneyfield );
        //t.setText( t.getText() + " -> " + app.getCurrentUser().getBalance() );



        return view;
    }


}
