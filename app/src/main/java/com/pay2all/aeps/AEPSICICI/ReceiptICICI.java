package com.pay2all.aeps.AEPSICICI;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ReceiptICICI extends AppCompatActivity {

    CardView cardview_receipt;
    ProgressDialog dialog;
    Uri file = null;

    LinearLayout ll_aadhaar;
    LinearLayout ll_utr;
    TextView textview_aadhaar;
    TextView textview_aadhaar_title;
    TextView textview_amount;
    TextView textview_balance;
    TextView textview_contact_number;
    TextView textview_date;
    TextView textview_number;
    TextView textview_bank;
    TextView textview_service_name;
    TextView textview_status;
    TextView textview_txnno;
    TextView textview_order_id;
    TextView textview_terminal_id;
    TextView textview_utr_title,textview_service,tv_description;
    String type = "";

    String payid="",txn_no_type="";

    LinearLayout ll_name;
    TextView textview_name;

    ImageView iv_copy;


    TextView tv_need_help;

    ImageView iv_status;

    RelativeLayout rl_receipt;

    TextView textview_shop_name,textview_agent_name,textview_contact_email;

    DBHelper dbHelper;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_receipt_new_icici);
        StrictMode.setVmPolicy(new Builder().build());
        
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DBHelper(this);


        rl_receipt=findViewById(R.id.rl_receipt);
        iv_status=findViewById(R.id.iv_status);

        tv_need_help=findViewById(R.id.tv_need_help);
//        tv_need_help.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ReceiptPaytm.this, Help.class));
//            }
//        });

        ll_name=findViewById(R.id.ll_name);



        iv_copy=findViewById(R.id.iv_copy);
        iv_copy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!payid.equals(""))
                {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", payid);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(ReceiptICICI.this, payid+ " Successfully copy to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textview_name=findViewById(R.id.textview_name);
        if (getIntent().hasExtra("name"))
        {
           String name=getIntent().getStringExtra("name");
           textview_name.setText(name);
           ll_name.setVisibility(View.VISIBLE);
        }

        textview_txnno = findViewById(R.id.textview_txnno);

        cardview_receipt =  findViewById(R.id.cardview_receipt);
        textview_amount = (TextView) findViewById(R.id.textview_amount);
        textview_status = (TextView) findViewById(R.id.textview_status);
        textview_number = (TextView) findViewById(R.id.textview_number);
        textview_bank = (TextView) findViewById(R.id.textview_bank);

        textview_date = (TextView) findViewById(R.id.textview_date);
        textview_balance = (TextView) findViewById(R.id.textview_balance);
        textview_contact_number = (TextView) findViewById(R.id.textview_contact_number);
        textview_aadhaar_title = (TextView) findViewById(R.id.textview_aadhaar_title);
        textview_utr_title = (TextView) findViewById(R.id.textview_order_id_title);
        ll_aadhaar = (LinearLayout) findViewById(R.id.ll_aadhaar);
        ll_utr = (LinearLayout) findViewById(R.id.ll_order);
        textview_order_id = (TextView) findViewById(R.id.textview_order_id);
        textview_service_name = (TextView) findViewById(R.id.textview_service_name);
        textview_aadhaar = (TextView) findViewById(R.id.textview_aadhaar);
        tv_description = (TextView) findViewById(R.id.tv_description);

        textview_terminal_id = findViewById(R.id.textview_terminal_id);

        textview_shop_name = findViewById(R.id.textview_shop_name);
        textview_agent_name = findViewById(R.id.textview_agent_name);
        textview_contact_email = findViewById(R.id.textview_contact_email);

        Intent intent=getIntent();
        if (intent.hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }

        String status=intent.getStringExtra("status");

        if (getIntent().hasExtra("data"))
        {
            String data=getIntent().getStringExtra("data");
            try {
                JSONObject jsonObject=new JSONObject(data);

                if (jsonObject.has("amount"))
                {
                    String amount=jsonObject.getString("amount");
                    textview_amount.setText(getResources().getString(R.string.rs)+amount);
                }

                if (jsonObject.has("balance"))
                {
                    String balance=jsonObject.getString("balance");
                    textview_balance.setText(getResources().getString(R.string.rs)+balance);
                }

                if (jsonObject.has("utr"))
                {
                     payid=jsonObject.getString("utr");
                    textview_txnno.setText("UTR No : "+payid);
                }

                if (jsonObject.has("terminal_id"))
                {
                    String terminal_id=jsonObject.getString("terminal_id");
                    textview_terminal_id.setText(terminal_id);
                }

                if (jsonObject.has("order_id"))
                {
                    String order_id=jsonObject.getString("order_id");
                    textview_order_id.setText(order_id);
                }

                if (jsonObject.has("message"))
                {
                    String message=jsonObject.getString("message");
                    tv_description.setText("Description : "+message);
                    tv_description.setVisibility(View.VISIBLE);
                }

                if (jsonObject.has("aadhar_number"))
                {
                    String aadhar_number=jsonObject.getString("aadhar_number");
                    textview_aadhaar.setText(aadhar_number);
                }

                if (jsonObject.has("bank_name"))
                {
                    String bank_name=jsonObject.getString("bank_name");
                    textview_bank.setText(bank_name);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        textview_status.setText(status);

        if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("success") || status.equalsIgnoreCase("credit")) {
            textview_status.setTextColor(getResources().getColor(R.color.green));
            textview_status.setText("Success");
//            tv_description.setVisibility(View.GONE);
            iv_status.setImageDrawable(getDrawable(R.drawable.check_icon));
            iv_status.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

        }
        else if (status.equalsIgnoreCase("2") ||status.equalsIgnoreCase("failure") || status.equalsIgnoreCase("debit")) {
            textview_status.setTextColor(getResources().getColor(R.color.red));
            textview_status.setText("Failure");
            tv_description.setVisibility(View.VISIBLE);

            iv_status.setImageDrawable(getDrawable(R.drawable.close_icon));
            iv_status.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        }
        else if (status.equalsIgnoreCase("pending")||status.equalsIgnoreCase("3")) {
            textview_status.setTextColor(getResources().getColor(R.color.orange));
            tv_description.setVisibility(View.VISIBLE);

            textview_status.setText("Process");
            iv_status.setImageDrawable(getDrawable(R.drawable.pending));

        }
        else {
            textview_status.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv_description.setVisibility(View.VISIBLE);
            iv_status.setImageDrawable(getDrawable(R.drawable.error_icon));
        }

        if (getIntent().hasExtra("number"))
        {
            textview_number.setText(intent.getStringExtra("number"));
        }

//        if (type.equalsIgnoreCase("recharge"))
//        {
//            textview_operator.setText(intent.getStringExtra("provider"));
//            textview_amount.setText("Rs "+intent.getStringExtra("amount"));
//            tv_description.setText(intent.getStringExtra("message"));

            textview_contact_number.setText(dbHelper.mGetMobile());
            textview_contact_email.setText(dbHelper.mGetEmail());
            textview_shop_name.setText(dbHelper.mGetCompany());
            textview_agent_name.setText(dbHelper.mGetName() +" "+dbHelper.mGetLastName());

            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy, hh:mm:ss");
            textview_date.setText(sdf.format(calendar.getTime()));

//        }
//        if (type.equals("money") || type.equals("account")) {
//            money_item = (Money_Transfer_Items) getIntent().getExtras().getSerializable("DATA");
//        } else {
//
//            item = (Recharge_Reports_Item) getIntent().getExtras().getSerializable("DATA");
//            type=item.getType();
//        }
        Button button = (Button) findViewById(R.id.button_finish);
        Button button2 = (Button) findViewById(R.id.button_download);

        textview_service=findViewById(R.id.textview_service);
        if (getIntent().hasExtra("service_name"))
        {
            textview_service.setText(getIntent().getStringExtra("service_name"));
        }

//        mShowData();
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (mCheckWriteStorage()) {

                    try {
                        mSaveImage(loadBitmapFromView(rl_receipt));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.STREAM", file);
                    startActivity(Intent.createChooser(intent, "Share Receipt"));

                }
                else {
                    mRequestWriteStorage();
                }
            }
        });

        if (getIntent().hasExtra("service"))
        {
            textview_service.setText(getIntent().getStringExtra("service"));
        }

    }

    /*
    public void mShowData() {
        String str;
        if (type.equals("account") || type.equals("money")) {
            TextView textView = textview_amount;
            StringBuilder sb = new StringBuilder();
            sb.append("Rs ");
            sb.append(money_item.getDebit());
            textView.setText(sb.toString());
        } else {
            TextView textView2 = textview_amount;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Rs ");
            sb2.append(item.getAmount());
            textView2.setText(sb2.toString());
        }
        if (type.equals("money") || type.equals("account")) {
            str = money_item.getStatus();
        } else {
            str = item.getStatus();
        }
        textview_status.setText(str);
        if (str.equalsIgnoreCase("success") || str.equalsIgnoreCase("credit")) {
            textview_status.setTextColor(getResources().getColor(R.color.green));
        } else if (str.equalsIgnoreCase("failure") || str.equalsIgnoreCase("debit")) {
            textview_status.setTextColor(getResources().getColor(R.color.red));
        } else if (str.equalsIgnoreCase("pending")) {
            textview_status.setTextColor(getResources().getColor(R.color.orange));
        } else {
            textview_status.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (type.equals("money") || type.equals("account")) {
            textview_txnno.setText(money_item.getTxnid());
            textview_date.setText(money_item.getDate());
            if (type.equals("account")) {
                textview_operator.setText(money_item.getProduct());
                textview_number.setText(money_item.getMobile());
            } else {
                textview_operator.setText(money_item.getBank());
                textview_number.setText(money_item.getAccount());
            }
            if (type.equalsIgnoreCase("money")) {
                ll_aadhaar.setVisibility(View.VISIBLE);
                textview_service_name.setText("Bank");
                textview_aadhaar_title.setText("IFSC");
                textview_aadhaar.setText(money_item.getIfsc());
                ll_utr.setVisibility(View.VISIBLE);
                textview_utr_title.setText("Charge");
                textview_utr.setText(money_item.getCharge());
            } else {
                textview_service_name.setText("Transaction Type");
            }
        } else {
            textview_number.setText(item.getNumber());
            textview_operator.setText(item.getCompany());
            textview_txnno.setText(item.getTxnno());
            textview_date.setText(item.getDate());
            TextView textView3 = textview_balance;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Rs ");
            sb3.append(item.getBalance());
            textView3.setText(sb3.toString());
            if (item.getType().equalsIgnoreCase("aeps") || item.getType().equalsIgnoreCase("aadhaar")) {
                ll_aadhaar.setVisibility(View.VISIBLE);
                textview_service_name.setText("Transaction Type");
                textview_aadhaar.setText(item.getAadhaar());
                ll_utr.setVisibility(View.VISIBLE);
                textview_utr.setText(item.getUtr());
            } else {
                ll_aadhaar.setVisibility(View.GONE);
            }
        }
        TextView textView4 = textview_contact;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Mobile : ");
        sb4.append(username);
        sb4.append("\nEmail : ");
        sb4.append(email);
        sb4.append("\nAddress : ");
        sb4.append(address);
        textView4.setText(sb4.toString());
    }


     */


    public static Bitmap loadBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return createBitmap;
    }


    private void mSaveImage(Bitmap bitmap) throws IOException {
        boolean saved;
        OutputStream fos;

        int nextInt = new Random().nextInt(10000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image-");
        sb2.append(nextInt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, sb2.toString());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" +  getResources().getString(R.string.app_name).replaceAll(" ","_"));
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);

            Log.e("uri","name "+imageUri.toString()+".png");

            file=imageUri;
//            file=new File(imageUri.toString()+".png");

        }

        else {

            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + getResources().getString(R.string.app_name).replaceAll(" ","_");

            file=Uri.fromFile(new File(imagesDir));
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


    public boolean mCheckWriteStorage() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void mRequestWriteStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}