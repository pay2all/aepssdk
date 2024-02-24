package com.pay2all.aeps.AEPSICICI.MiniStateListData;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MiniStatementLIst extends AppCompatActivity {
    CardView cardview_receipt;
    Uri file = null;
    Button imageview_close;
    Button imageview_share;
    LinearLayout ll_all_detail;
    MiniStatementCardAdapter miniStatementCardAdapter;
    List<MiniStatementItems> miniStatementItems;
    RecyclerView recyclerview_mini_statement;
    TextView textview_aadhaar_number;
    TextView textview_available_balance;
    TextView textview_bank_receipt;
    TextView textview_customer_mob;
    TextView textview_message;
    TextView textview_order_id;
    TextView textview_utr;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_mini_statement_list);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.cardview_receipt =  findViewById(R.id.cardview_receipt);
        this.imageview_close =  findViewById(R.id.imageview_close);
        this.textview_available_balance =  findViewById(R.id.textview_available_balance);
        this.textview_utr =  findViewById(R.id.textview_utr);
        this.textview_order_id =  findViewById(R.id.textview_order_id);
        this.textview_customer_mob =  findViewById(R.id.textview_customer_mob);
        this.textview_aadhaar_number =  findViewById(R.id.textview_aadhaar_number);
        this.textview_message =  findViewById(R.id.textview_message);
        this.textview_bank_receipt =  findViewById(R.id.textview_bank);
        this.ll_all_detail =  findViewById(R.id.ll_all_detail);
        this.imageview_share =  findViewById(R.id.imageview_share);
        this.recyclerview_mini_statement =  findViewById(R.id.recyclerview_mini_statement);
        this.recyclerview_mini_statement.setLayoutManager(new LinearLayoutManager(this));
        this.miniStatementItems = new ArrayList();
        this.miniStatementCardAdapter = new MiniStatementCardAdapter(this, this.miniStatementItems);
        this.recyclerview_mini_statement.setAdapter(this.miniStatementCardAdapter);
        this.imageview_close.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                finish();
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
        String str4 = str;
        String str5 = "mini_statement";
        String str6 = "bank_name";
        String str7 = "order_id";
        String str8 = "utr";
        String str9 = "balance";
        String str10 = "message";
        String str11 = "statusCode";
        String str12 = NotificationCompat.CATEGORY_STATUS;
        String str13 = "";
        if (!str4.equals(str13)) {
            this.ll_all_detail.setVisibility(View.VISIBLE);
            this.textview_message.setVisibility(View.GONE);
            try {
                JSONObject jSONObject = new JSONObject(str4);
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
                    StringBuilder sb = new StringBuilder();
                    sb.append(jSONArray);
                    sb.append(str13);
                    Log.e("data", sb.toString());
                    for (int i = 0; i < jSONArray.length(); i++) {
                        MiniStatementItems miniStatementItems2 = new MiniStatementItems();
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        miniStatementItems2.setAmount(jSONObject2.getString("amount"));
                        miniStatementItems2.setDate(jSONObject2.getString("date"));
                        miniStatementItems2.setTxnType(jSONObject2.getString("txnType"));
                        miniStatementItems2.setNarration(jSONObject2.getString("narration"));
                        this.miniStatementItems.add(miniStatementItems2);
                        this.miniStatementCardAdapter.notifyDataSetChanged();
                    }
                }

                this.ll_all_detail.setVisibility(View.VISIBLE);
                this.textview_message.setVisibility(View.GONE);
                TextView textView = this.textview_available_balance;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Rs ");
                sb2.append(string3);
                textView.setText(sb2.toString());
                this.textview_utr.setText(string4);
                this.textview_order_id.setText(string5);
                this.textview_customer_mob.setText(str2);
                TextView textView2 = this.textview_aadhaar_number;


                if (str3.length()>8) {
                    String sb3 = "XXXX-XXXX-" + str3.substring(8);
                    textView2.setText(sb3);
                }


                this.textview_bank_receipt.setText(string6);
//                if (string.equals(str13)) {
//                    this.imageview_share.setVisibility(View.GONE);
//                    this.textview_message.setVisibility(View.VISIBLE);
//                    this.ll_all_detail.setVisibility(View.GONE);
//                    this.textview_message.setText(string2);
//                }
//                else if (string.equals("0")) {
//
//                } else {
//                    this.imageview_share.setVisibility(View.GONE);
//                    this.textview_message.setVisibility(View.VISIBLE);
//                    this.ll_all_detail.setVisibility(View.GONE);
//                    if (!string2.equals(str13)) {
//                        this.textview_message.setText(string2);
//                    } else {
//                        this.textview_message.setText("Something went wrong please try again later");
//                    }
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            this.imageview_share.setVisibility(View.GONE);
            this.ll_all_detail.setVisibility(View.GONE);
            this.textview_message.setVisibility(View.VISIBLE);
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
