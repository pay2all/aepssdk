package com.pay2all.aeps.PaytmAEPS;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pay2all.aeps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class TransactionReceipt extends AppCompatActivity {

    File file=null;

    boolean z;
    String str3 = "order_id";
    String str4 = "utr";
    String str5 = "balance";
    String str6 = "message";
    String str7 = "status_id";

    String data="",number="",aadhaar_number="",bank="";
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_transction_receipt_paytm);

        TextView textView = (TextView) findViewById(R.id.textview_available_balance);
        TextView textView2 = (TextView) findViewById(R.id.textview_utr);
        TextView textView3 = (TextView) findViewById(R.id.textview_order_id);
        TextView textView4 = (TextView) findViewById(R.id.textview_customer_mob);
        TextView textView5 = (TextView) findViewById(R.id.textview_aadhaar_number);
        TextView textView6 = (TextView) findViewById(R.id.textview_message);
        ImageView imageView = (ImageView) findViewById(R.id.imageview_close);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_all_detail);
        TextView textView7 = (TextView) findViewById(R.id.textview_bank);

        TextView textview_service =  findViewById(R.id.textview_service);
        TextView textview_amount =  findViewById(R.id.textview_amount);

        ImageView imageView2 = (ImageView) findViewById(R.id.imageview_share);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckWriteStorage()) {
                    mSaveFile(loadBitmapFromView(linearLayout));
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                    startActivity(Intent.createChooser(intent, "Share Receipt"));
                    finish();
                    return;
                }
                mRequestWriteStorage();
            }
        });

        textview_service.setText("Balance Enquiry");
        String str8 = "";
        if (!data.equals(str8)) {
            linearLayout.setVisibility(View.VISIBLE);
            textView6.setVisibility(View.GONE);
            try {
                JSONObject jSONObject = new JSONObject(data);

                String string = jSONObject.has(str7) ? jSONObject.getString(str7) : str8;
                String string2 = jSONObject.has(str6) ? jSONObject.getString(str6) : str8;
                String string3 = jSONObject.has(str5) ? jSONObject.getString(str5) : str8;
                String string4 = jSONObject.has(str4) ? jSONObject.getString(str4) : str8;
                String string5 = jSONObject.has(str3) ? jSONObject.getString(str3) : str8;

                if (jSONObject.has("amount"))
                {
                    String amount=jSONObject.getString("amount");
                    textview_amount.setText("Rs "+amount);
                }
                else
                {
                    textview_amount.setText("Rs 0.0");
                }

                linearLayout.setVisibility(View.VISIBLE);
                textView6.setVisibility(View.GONE);
                textView2.setText(string4);
                textView3.setText(string5);
                StringBuilder sb = new StringBuilder();
                sb.append("Rs ");
                sb.append(string3);
                textView.setText(sb.toString());
                textView4.setText(number);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("XXXX-XXXX-");
                sb2.append(aadhaar_number.substring(8, aadhaar_number.length()));
                textView5.setText(sb2.toString());
                textView7.setText(bank);

                textView6.setText(string2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            z = false;
        } else {
            imageView2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            z = false;
            textView6.setVisibility(View.VISIBLE);
            textView6.setText("Server not responsing, please try again later...");
        }
    }

    public static Bitmap loadBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return createBitmap;
    }

    public void mSaveFile(Bitmap bitmap) {
        String file2 = Environment.getExternalStorageDirectory().toString();
        StringBuilder sb = new StringBuilder();
        sb.append(file2);
        sb.append("/");
        sb.append(getResources().getString(R.string.app_name));
        sb.append("_receipt");
        File file3 = new File(sb.toString());
        file3.mkdirs();
        int nextInt = new Random().nextInt(10000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image-");
        sb2.append(nextInt);
        sb2.append(".jpg");
        this.file = new File(file3, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(this.file);
        Log.i("Freelinfing", sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Receipt Downloaded ");
        sb4.append(this.file);
        Toast.makeText(this, sb4.toString(), Toast.LENGTH_SHORT).show();
        if (this.file.exists()) {
            this.file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(this.file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception unused) {
            unused.printStackTrace();
        }
    }

    public boolean mCheckWriteStorage() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void mRequestWriteStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }
}