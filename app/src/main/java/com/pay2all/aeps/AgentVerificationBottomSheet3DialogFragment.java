package com.pay2all.aeps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pay2all.aeps.AgentVerifyDetail.VerifyAgent;


/**
 * Created by Basant on 3/29/2018.
 */

public class AgentVerificationBottomSheet3DialogFragment extends BottomSheetDialogFragment {

    public static String activity_name;
    
    private BottomSheetBehavior mBehavior;

    Button bt_verify_now;

    ImageView iv_close;

    public  AgentVerificationBottomSheet3DialogFragment dialogFragment;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.bottom_sheet_fragement_agent_verification, null);

        dialogFragment=this;


//        LinearLayout linearLayout = view.findViewById(R.id.root);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
//        params.height = Resources.getSystem().getDisplayMetrics().heightPixels;
//        linearLayout.setLayoutParams(params);

        bt_verify_now=view.findViewById(R.id.bt_verify_now);
        bt_verify_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VerifyAgent.class);
                if (activity_name.equals("aadhaar")) {
                    intent.putExtra("aadhaar_verify","1");
                }
                startActivity(intent);
            }
        });

        iv_close=view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        activity_name=getArguments().getString("activity");

        TextView tv_partial_text=view.findViewById(R.id.tv_partial_text);

        String partialText="According to NPCI guidelines Retailer should \nperform <font color='#0620AF'>Retailer Biometric Verification</font> before\nEvery financial transaction ";

        tv_partial_text.setText(Html.fromHtml(partialText));

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
//        mBehavior.setDraggable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,new IntentFilter("agent_verification"));

        return dialog;

    }
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("status"))
            {
                String status=intent.getStringExtra("status");
                if (!status.equals(""))
                {
                    if (status.equals("1")||status.equals("true"))
                    {
                        dismissAllowingStateLoss();
//                        dialogFragment.dismiss();
//                        Toast.makeText(context, "closed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

}