package com.pay2all.aeps.AgentVerifyDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.pay2all.aeps.R;

import java.util.List;

public class FingerDeviceCardAdapter extends Adapter<FingerDeviceCardAdapter.ViewHolder> {
    Context context;
    List<FingerDevicesItems> devicesItems;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview_device_name;

        ViewHolder(View view) {
            super(view);
            this.textview_device_name =  view.findViewById(R.id.textview_device_name);

            view.setOnClickListener(view1 -> {
                FingerDevicesItems items =  devicesItems.get(ViewHolder.this.getAdapterPosition());

                ((VerifyAgent) context).mGetData(items);

            });
        }
    }

    public FingerDeviceCardAdapter(Context context2, List<FingerDevicesItems> list) {
        this.context = context2;
        this.devicesItems = list;
    }

    public int getItemCount() {
        List<FingerDevicesItems> list = this.devicesItems;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.textview_device_name.setText( (this.devicesItems.get(i)).getName());
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.devices_item, viewGroup, false));
    }
}
