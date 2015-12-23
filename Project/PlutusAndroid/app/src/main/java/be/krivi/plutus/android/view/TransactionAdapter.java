package be.krivi.plutus.android.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.model.Transaction;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.util.List;

/**
 * Created by Krivi on 23/12/15.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

    private LayoutInflater inflater;
    private List<Transaction> transactions;

    public TransactionAdapter( Context context, List<Transaction> transactions ){
        inflater = LayoutInflater.from( context );
    }

    @Override
    public TransactionViewHolder onCreateViewHolder( ViewGroup parent, int viewType ){
        return new TransactionViewHolder( inflater.inflate( R.layout.row_transaction, parent, false ) );
    }

    @Override
    public void onBindViewHolder( TransactionViewHolder holder, int position ){
        Transaction transaction = transactions.get( position );

        //TODO localize transaction data

        holder.mMonth.setText( transaction.getMonth() + "" );
        holder.mDay.setText( transaction.getDay() + "" );

        holder.mTitle.setText( transaction.getTitle() );
        holder.mLocation.setText( transaction.getLocation().getName() );

        //
        holder.mAmount.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + transaction.getAmount() );

        //TODO set font color of amount according to type
        holder.transactionType = transaction.getType();
    }

    @Override
    public int getItemCount(){
        return 0;
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder{

        @Bind( R.id.tr_wrapperDate )
        RelativeLayout mWrapperDate;

        @Bind( R.id.tr_month )
        TextView mMonth;

        @Bind( R.id.tr_day )
        TextView mDay;

        @Bind( R.id.tr_title )
        TextView mTitle;

        @Bind( R.id.tr_location )
        TextView mLocation;

        @Bind( R.id.tr_amount )
        TextView mAmount;

        public String transactionType;

        public TransactionViewHolder( View view ){
            super( view );
            ButterKnife.bind( this, view );
        }
    }
}
