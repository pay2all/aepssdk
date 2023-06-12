package com.pay2all.aeps.Reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pay2all.aeps.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionReportsCardAdapter extends RecyclerView.Adapter<TransactionReportsCardAdapter.ViewHolder> {


    Context context;
    List<TransactionReportsItem> transactionReportsItems;
    public TransactionReportsCardAdapter(Context context, List<TransactionReportsItem> transactionReportsItems)
    {
        this.context=context;
        this.transactionReportsItems=transactionReportsItems;
    }

    @Override
    public int getItemCount() {
        return transactionReportsItems==null ?0:transactionReportsItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_reports_items,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionReportsItem items=transactionReportsItems.get(position);
        holder.textview_id.setText("Id : "+items.getId());
        holder.textview_amount.setText("Amount : Rs "+items.getAmount());
        holder.textview_txnid.setText("Txnid : "+items.getTxnid());
        holder.textview_number.setText("Number : "+items.getNumber());
        holder.textview_provider.setText("Provider : "+items.getProvider());
        holder.textview_total_balance.setText("Total Balance : "+items.getTotal_balance());
        holder.textview_commisson.setText("Commission : "+items.getCommisson());
        holder.textview_date.setText("Date  : "+items.getDate());

        if (items.getStatus().equalsIgnoreCase("success"))
        {
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (items.getStatus().equalsIgnoreCase("Debit"))
        {
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if (items.getStatus().equalsIgnoreCase("credit"))
        {
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if (items.getStatus().equalsIgnoreCase("failure"))
        {
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.red));
        }
        else
        {
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.orange));
        }

        holder.textview_status.setText(items.getStatus());


        holder.setIsRecyclable(false);

        if (position==transactionReportsItems.size()-1)
        {
            if (TransactionReports.last_array_empty)
            {

            }
            else
            {
                ((TransactionReports)context).mGetNextPageData();
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_id,textview_amount,textview_txnid,textview_number,textview_provider
                ,textview_total_balance,textview_commisson,textview_date,textview_status;
        ViewHolder(View view)
        {
            super(view);

            textview_id=view.findViewById(R.id.textview_id);
            textview_amount=view.findViewById(R.id.textview_amount);
            textview_txnid=view.findViewById(R.id.textview_txnid);
            textview_number=view.findViewById(R.id.textview_number);
            textview_provider=view.findViewById(R.id.textview_provider);
            textview_total_balance=view.findViewById(R.id.textview_total_balance);
            textview_commisson=view.findViewById(R.id.textview_commisson);
            textview_date=view.findViewById(R.id.textview_date);
            textview_status=view.findViewById(R.id.textview_status);
        }
    }


    public void UpdateList(List<TransactionReportsItem> item)
    {
        transactionReportsItems=item;
        notifyDataSetChanged();
    }
}
