package com.pay2all.aeps.AEPSICICI.BankLIst;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.pay2all.aeps.aepsnew.AEPSNewService;
import com.pay2all.aeps.Constants;
import com.pay2all.aeps.AEPSICICI.AadhaarPay;
import com.pay2all.aeps.AEPSICICI.BalaneEnquiry;
import com.pay2all.aeps.AEPSICICI.MiniStatement;
import com.pay2all.aeps.AEPSICICI.Withdrawal;

import com.pay2all.aeps.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfAllBanksCardAdapter extends RecyclerView.Adapter<ListOfAllBanksCardAdapter.ViewHolder> {

    Context context;
    List<BankListItems> bankListItems;

    public ListOfAllBanksCardAdapter(Context context,List<BankListItems> bankListItems)
    {
        this.context=context;
        this.bankListItems=bankListItems;
    }

    @Override
    public int getItemCount() {
        return bankListItems==null?0:bankListItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BankListItems items=bankListItems.get(position);

        if (items.getIcon().equals(""))
        {
            holder.imageview_operator_icon.setVisibility(View.GONE);
            holder.textview_capital_latter_operator.setVisibility(View.VISIBLE);
            holder.textview_capital_latter_operator.setText(items.getBank_name().substring(0,1));
        }


        holder.textview_operator.setText(items.getBank_name());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bank_list_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageview_operator_icon;
        TextView textview_capital_latter_operator,textview_operator;

        ViewHolder (View view)
        {
            super(view);
            imageview_operator_icon=view.findViewById(R.id.imageview_operator_icon);
            textview_capital_latter_operator=view.findViewById(R.id.textview_capital_latter_operator);
            textview_operator=view.findViewById(R.id.textview_operator);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BankListBottomSheet3DialogFragment.dialogFragment.dismiss();
                    Constants.bank_name=bankListItems.get(getAdapterPosition()).getBank_name();
                    if (BankListBottomSheet3DialogFragment.activity_name.equalsIgnoreCase("mini")) {
                        ((MiniStatement) ListOfAllBanksCardAdapter.this.context).mGetBankDetail((BankListItems) ListOfAllBanksCardAdapter.this.bankListItems.get(ViewHolder.this.getAdapterPosition()));
                    } else if (BankListBottomSheet3DialogFragment.activity_name.equalsIgnoreCase("balance")) {
                        ((BalaneEnquiry) ListOfAllBanksCardAdapter.this.context).mGetBankDetail((BankListItems) ListOfAllBanksCardAdapter.this.bankListItems.get(ViewHolder.this.getAdapterPosition()));
                    } else if (BankListBottomSheet3DialogFragment.activity_name.equalsIgnoreCase("aadhaar")) {
                        ((AadhaarPay) ListOfAllBanksCardAdapter.this.context).mGetBankDetail((BankListItems) ListOfAllBanksCardAdapter.this.bankListItems.get(ViewHolder.this.getAdapterPosition()));
                    }
                    else if (BankListBottomSheet3DialogFragment.activity_name.equalsIgnoreCase("with")) {
                        ((Withdrawal) ListOfAllBanksCardAdapter.this.context).mGetBankDetail((BankListItems) ListOfAllBanksCardAdapter.this.bankListItems.get(ViewHolder.this.getAdapterPosition()));
                    }
                    else if (BankListBottomSheet3DialogFragment.activity_name.equalsIgnoreCase("withnew")) {
                        ((AEPSNewService) ListOfAllBanksCardAdapter.this.context).mGetBankDetail( bankListItems.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public void UpdateList(List<BankListItems> item)
    {
        bankListItems=item;
        notifyDataSetChanged();
    }
}
