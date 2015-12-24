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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Created by Krivi on 23/12/15.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

    private LayoutInflater inflater;
    private List<Transaction> transactions;

    DecimalFormat df;

    public TransactionAdapter( Context context, List<Transaction> transactions ){
        inflater = LayoutInflater.from( context );
        this.transactions = transactions;

        df = new DecimalFormat( "#.00", DecimalFormatSymbols.getInstance( Locale.getDefault() ) );
    }

    @Override
    public TransactionViewHolder onCreateViewHolder( ViewGroup parent, int viewType ){
        return new TransactionViewHolder( inflater.inflate( R.layout.row_transaction, parent, false ) );
    }

    @Override
    public void onBindViewHolder( TransactionViewHolder holder, int position ){
        Transaction transaction = transactions.get( position );

        holder.mMonth.setText( transaction.getMonth( "short" ) );
        holder.mDay.setText( transaction.getDay() + "" );

        holder.mTitle.setText( transaction.getTitle() );
        holder.mLocation.setText( transaction.getLocation().getName() );

        //TODO Jan remove this line below whem timsestamp is 100% fixed -- date and time (Possibly already ok because I set the system locale)
        holder.mLocation.setText( transaction.getTimestamp().toString() );

        holder.mAmount.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + df.format( transaction.getAmount() ) );

        //TODO set font color of amount according to type
        holder.transactionType = transaction.getType();
    }

    @Override
    public int getItemCount(){
        return transactions.size();
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
