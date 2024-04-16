package com.pay2all.aeps.AEPSICICI;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pay2all.aeps.Constants;
import com.pay2all.aeps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ReceiptTest extends AppCompatActivity {

    String number="",aadhaar_number="",bank="";
    String str="";
    String str2 = str;
    String provider_id = "provider_id";
    String str3 = "order_id";
    String str4 = "utr";
    String str5 = "balance";
    String str6 = "message";
    String str7 = "status";
    String amount="";
    TextView textview_agent_id,textview_bc_name,textview_amount;
    Button button_save,button_share;

    CardView cardview_receipt;
    Uri file=null;

    TextView textview_service;
    ImageView iv_status;

    boolean mIsSaved=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_receipt);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        Bundle bundle=getIntent().getExtras();
        if (bundle.containsKey("data"))
        {
            str2=bundle.getString("data");
        }

        if (bundle.containsKey("number"))
        {
            number=bundle.getString("number");
        }
        if (bundle.containsKey("aadhaar"))
        {
            aadhaar_number=bundle.getString("aadhaar");
        }

        if (bundle.containsKey("bank"))
        {
            bank=bundle.getString("bank");
        }

        textview_service=findViewById(R.id.textview_service);
        if (bundle.containsKey("service"))
        {
            textview_service.setText(bundle.getString("service"));
        }

        iv_status=findViewById(R.id.iv_status);
        TextView textView = (TextView) findViewById(R.id.textview_available_balance);
        TextView textView2 = (TextView) findViewById(R.id.textview_utr);
        TextView textView3 = (TextView) findViewById(R.id.textview_order_id);
        TextView textView4 = (TextView) findViewById(R.id.textview_customer_mob);
        TextView textView5 = (TextView) findViewById(R.id.textview_aadhaar_number);
        TextView textView6 = (TextView) findViewById(R.id.textview_message);
        ImageView imageView = (ImageView) findViewById(R.id.iv_close);
        textview_agent_id=findViewById(R.id.textview_agent_id);
        textview_bc_name=findViewById(R.id.textview_bc_name);
        textview_amount=findViewById(R.id.textview_amount);
        TextView textView7 = (TextView) findViewById(R.id.textview_bank);
        String str8 = "";
        TextView textview_aadhaar_number = textView5;

        cardview_receipt=findViewById(R.id.cardview_receipt);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        button_save=findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                mIsSaved=true;
//
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S)
//                {
//                    mSaveFile(loadBitmapFromView(cardview_receipt));
//                }
//                else {
//
//                    if (mCheckWriteStorage()) {
//                        mSaveFile(loadBitmapFromView(cardview_receipt));
//                    } else {
//                        mRequestWriteStorage();
//                    }
//                }

            }
        });

        button_share=findViewById(R.id.button_share);
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsSaved=false;

                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S)
                {
                    mSaveFile(loadBitmapFromView(cardview_receipt));
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
//                    Uri uri = Uri.fromFile(file);
                    share.putExtra(Intent.EXTRA_STREAM, file);
                    startActivity(Intent.createChooser(share, "Share Receipt"));
                    finish();
                }

                else {
                    if (mCheckWriteStorage()) {
                        mSaveFile(loadBitmapFromView(cardview_receipt));
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
//                    Uri uri = Uri.fromFile(file);
                        share.putExtra(Intent.EXTRA_STREAM, file);
                        startActivity(Intent.createChooser(share, "Share Receipt"));
                        finish();
                    }
                    else {
                        mRequestWriteStorage();
                    }
                }

            }
        });

        if (!str2.equals(str8)) {
            try {
                JSONObject jSONObject = new JSONObject(str2);
                String string = jSONObject.has(str7) ? jSONObject.getString(str7) : str8;
                String string2 = jSONObject.has(str6) ? jSONObject.getString(str6) : str8;
                String string3 = jSONObject.has(str5) ? jSONObject.getString(str5) : str8;
                String string4 = jSONObject.has(str4) ? jSONObject.getString(str4) : str8;
                String string5 = jSONObject.has(str3) ? jSONObject.getString(str3) : str8;

                if (jSONObject.has("amount")){
                    amount=jSONObject.getString("amount");
                }

                if (jSONObject.has("aadhaar_number")){
                    aadhaar_number=jSONObject.getString("aadhaar_number");
                }

                if (string.equals("0")||string.equals("1"))
                {
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.success));
                }
                else if (string.equals("2"))
                {
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.failed_icon));
                }
                else if (Constants.service_id.contains("3")&&string.equals("1"))
                {
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.success));
                }
                else
                {
                    iv_status.setImageDrawable(getResources().getDrawable(R.drawable.error_icon));

                }

                textview_amount.setText("Rs "+amount);
                textview_agent_id.setText(Constants.outlet_id);
                textview_bc_name.setText(Constants.name);

                textView6.setText(string2);

                textView2.setText(string4);
                textView3.setText(string5);
                StringBuilder sb = new StringBuilder();
                sb.append("Rs ");
                sb.append(string3);
                textView.setText(sb.toString());
                textView4.setText(this.number);

                textView7.setText(Constants.bank_name);
                textview_aadhaar_number.setText(aadhaar_number);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

    //    for save bitmap as a image
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

        if (saved) {
            if (mIsSaved) {
                Toast.makeText(this, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
            }
        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // for write extrenal storage
    public boolean mCheckWriteStorage() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void mRequestWriteStorage()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

}
