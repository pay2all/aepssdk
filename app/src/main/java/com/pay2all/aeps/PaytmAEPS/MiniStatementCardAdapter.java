package com.pay2all.aeps.PaytmAEPS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.pay2all.aeps.R;

import java.util.List;

public class MiniStatementCardAdapter extends Adapter<MiniStatementCardAdapter.ViewHolder> {
    Context context;
    List<com.pay2all.aeps.PaytmAEPS.MiniStatementItems> miniStatementItems;

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView textview_amount;
        TextView textview_date;
        TextView textview_maration;
        TextView textview_type;

        ViewHolder(View view) {
            super(view);
            this.textview_amount = (TextView) view.findViewById(R.id.textview_amount);
            this.textview_type = (TextView) view.findViewById(R.id.textview_type);
            this.textview_date = (TextView) view.findViewById(R.id.textview_date);
            this.textview_maration = (TextView) view.findViewById(R.id.textview_maration);
        }
    }

    public MiniStatementCardAdapter(Context context2, List<com.pay2all.aeps.PaytmAEPS.MiniStatementItems> list) {
        this.context = context2;
        this.miniStatementItems = list;
    }

    public int getItemCount() {
        List<com.pay2all.aeps.PaytmAEPS.MiniStatementItems> list = this.miniStatementItems;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        com.pay2all.aeps.PaytmAEPS.MiniStatementItems miniStatementItems2 = (com.pay2all.aeps.PaytmAEPS.MiniStatementItems) this.miniStatementItems.get(i);
        TextView textView = viewHolder.textview_amount;

        textView.setText(miniStatementItems2.getAmount());
        viewHolder.textview_type.setText(miniStatementItems2.getTxnType());
        TextView textView2 = viewHolder.textview_date;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Date ");
        sb2.append(miniStatementItems2.getDate());
        textView2.setText(sb2.toString());
        viewHolder.textview_maration.setText(miniStatementItems2.getNarration());
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.mini_statement_list_item_paytm, viewGroup, false));
    }
}
