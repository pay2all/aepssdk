package com.pay2all.aeps.PaytmAEPS;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pay2all.aeps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiniStatementLIst extends AppCompatActivity {
    CardView cardview_receipt;
    Uri file = null;
    ImageView imageview_close;
    ImageView imageview_share;
    LinearLayout ll_all_detail;
    MiniStatementCardAdapter miniStatementCardAdapter;
    List<com.pay2all.aeps.PaytmAEPS.MiniStatementItems> miniStatementItems;
    RecyclerView recyclerview_mini_statement;
    TextView textview_aadhaar_number;
    TextView textview_available_balance;
    TextView textview_bank_receipt;
    TextView textview_customer_mob;
    TextView textview_message;
    TextView textview_order_id;
    TextView textview_utr;

    String simpledata="{\"status\":0,\"status_id\":1,\"amount\":0,\"balance\":0,\"utr\":\"114417068617\",\"terminal_id\":\"9024814346\",\"order_id\":\"9024814346\",\"message\":\"Transaction is successful\",\"mini_statement\":[\"2802  CINTD0978074886  000000131.00\",\"3011  CINTD0978029378  000000131.00\",\"3108  CINTD0978098808  000000131.00\",\"3105  CINTD0978143555  000000145.00\",\"2902  CINTD0978137137  000000149.00\",\"3011  CINTD0978672723  000000148.00\",\"3108  CINTD0979428517  000000148.00\",\"3105  CINTD0980230908  000000147.00\",\"2802  CINTD0981343240  000000142.00\",\"            Balance : 0000017776.00\"],\"error_message\":\"Transaction is successful\",\"aadhar_number\":\"482606997231\",\"bank_name\":\"Central Bank of India\"}\n";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());

        setContentView((int) R.layout.activity_mini_statement_list_paytm);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        this.cardview_receipt = (CardView) findViewById(R.id.cardview_receipt);
        this.imageview_close = (ImageView) findViewById(R.id.imageview_close);
        this.textview_available_balance = (TextView) findViewById(R.id.textview_available_balance);
        this.textview_utr = (TextView) findViewById(R.id.textview_utr);
        this.textview_order_id = (TextView) findViewById(R.id.textview_order_id);
        this.textview_customer_mob = (TextView) findViewById(R.id.textview_customer_mob);
        this.textview_aadhaar_number = (TextView) findViewById(R.id.textview_aadhaar_number);
        this.textview_message = (TextView) findViewById(R.id.textview_message);
        this.textview_bank_receipt = (TextView) findViewById(R.id.textview_bank);
        this.ll_all_detail = (LinearLayout) findViewById(R.id.ll_all_detail);
        this.imageview_share = (ImageView) findViewById(R.id.imageview_share);
        this.recyclerview_mini_statement = (RecyclerView) findViewById(R.id.recyclerview_mini_statement);
        this.recyclerview_mini_statement.setLayoutManager(new LinearLayoutManager(this));
        this.miniStatementItems = new ArrayList();
        this.miniStatementCardAdapter = new MiniStatementCardAdapter(this, this.miniStatementItems);
        this.recyclerview_mini_statement.setAdapter(this.miniStatementCardAdapter);
        this.imageview_close.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MiniStatementLIst.this.finish();
            }
        });
        mShowData(getIntent().getStringExtra("data"), getIntent().getStringExtra("number"), getIntent().getStringExtra("aadhaar"));
        this.imageview_share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MiniStatementLIst.this.mCheckWriteStorage()) {
                    MiniStatementLIst miniStatementLIst = MiniStatementLIst.this;
                    miniStatementLIst.mSaveFile(MiniStatementLIst.loadBitmapFromView(miniStatementLIst.cardview_receipt));
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.STREAM", file);
                    MiniStatementLIst.this.startActivity(Intent.createChooser(intent, "Share Receipt"));
                    MiniStatementLIst.this.finish();
                    return;
                }
                MiniStatementLIst.this.mRequestWriteStorage();
            }
        });
    }

    public void mShowData(String str, String str2, String str3) {
        String str5 = "mini_statement";
        String str6 = "bank_name";
        String str7 = "order_id";
        String str8 = "utr";
        String str9 = "balance";
        String str10 = "message";
        String str11 = "statusCode";
        String str12 = NotificationCompat.CATEGORY_STATUS;
        String str13 = "";
        if (!str.equals(str13)) {
            this.ll_all_detail.setVisibility(0);
            this.textview_message.setVisibility(8);
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.has(str12) ? jSONObject.getString(str12) : str13;
                if (jSONObject.has(str11)) {
                    jSONObject.getString(str11);
                }
                String string2 = jSONObject.has(str10) ? jSONObject.getString(str10) : str13;
                String string3 = jSONObject.has(str9) ? jSONObject.getString(str9) : str13;
                String string4 = jSONObject.has(str8) ? jSONObject.getString(str8) : str13;
                String string5 = jSONObject.has(str7) ? jSONObject.getString(str7) : str13;
                String string6 = jSONObject.has(str6) ? jSONObject.getString(str6) : str13;
                if (jSONObject.has(str5)) {
                    JSONArray jSONArray = jSONObject.getJSONArray(str5);
                    String sb = jSONArray +
                            str13;
                    Log.e("data", sb);

                    textview_available_balance.setText(jSONArray.getString(jSONArray.length() - 1).replaceAll(" ",""));


                    for (int i = 0; i < jSONArray.length(); i++) {
                        com.pay2all.aeps.PaytmAEPS.MiniStatementItems miniStatementItems2 = new com.pay2all.aeps.PaytmAEPS.MiniStatementItems();
//                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);

                        String string_values=jSONArray.getString(i);

                        miniStatementItems2.setAmount(string_values);

//                        miniStatementItems2.setTxnType(jSONObject2.getString("txnType"));
//                        miniStatementItems2.setNarration(jSONObject2.getString("narration"));
                        this.miniStatementItems.add(miniStatementItems2);
                        this.miniStatementCardAdapter.notifyDataSetChanged();
                    }
                }
                if (string.equals(str13)) {
                    this.imageview_share.setVisibility(8);
                    this.textview_message.setVisibility(0);
                    this.ll_all_detail.setVisibility(8);
                    this.textview_message.setText(string2);
                } else if (string.equals("0")) {
                    this.ll_all_detail.setVisibility(0);
                    this.textview_message.setVisibility(8);
//                    TextView textView = this.textview_available_balance;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Rs ");
                    sb2.append(string3);
//                    textView.setText(sb2.toString());
                    this.textview_utr.setText(string4);
                    this.textview_order_id.setText(string5);
                    this.textview_customer_mob.setText(str2);
                    TextView textView2 = this.textview_aadhaar_number;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("XXXX-XXXX-");
                    sb3.append(str3.substring(8, str3.length()));
                    textView2.setText(sb3.toString());
                    this.textview_bank_receipt.setText(string6);
                } else {
                    this.imageview_share.setVisibility(8);
                    this.textview_message.setVisibility(0);
                    this.ll_all_detail.setVisibility(8);
                    if (!string2.equals(str13)) {
                        this.textview_message.setText(string2);
                    } else {
                        this.textview_message.setText("Something went wrong please try again later");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            this.imageview_share.setVisibility(8);
            this.ll_all_detail.setVisibility(8);
            this.textview_message.setVisibility(0);
            this.textview_message.setText("Server not responsing, please try again later...");
        }
    }

    public static Bitmap loadBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getLayoutParams().width, view.getLayoutParams().height, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return createBitmap;
    }

    /* access modifiers changed from: protected */
    public void mSaveFile(Bitmap bitmap) {
        boolean saved;
        OutputStream fos;

        int nextInt = new Random().nextInt(10000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image-");
        sb2.append(nextInt);

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, sb2.toString());
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + getResources().getString(R.string.app_name).replaceAll(" ", "_"));
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);

                Log.e("uri", "name " + imageUri.toString() + ".png");

                file = imageUri;
//            file=new File(imageUri.toString()+".png");

            } else {

                String imagesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + getResources().getString(R.string.app_name).replaceAll(" ", "_");

                file = Uri.fromFile(new File(imagesDir));
                File file = new File(imagesDir);

                if (!file.exists()) {
                    file.mkdir();
                }

                file = new File(imagesDir, sb2.toString() + ".png");
                fos = new FileOutputStream(file);

            }

            saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

//        if (saved) {
//            Toast.makeText(context, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
//        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean mCheckWriteStorage() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    /* access modifiers changed from: private */
    public void mRequestWriteStorage() {
        String str = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
            ActivityCompat.requestPermissions(this, new String[]{str}, 1);
        }
    }


}
