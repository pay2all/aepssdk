package com.pay2all.aeps.PaytmAEPS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pay2all.aeps.Constants;
import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.PaytmAEPS.BankLIst.BankListBottomSheet3DialogFragment;
import com.pay2all.aeps.PaytmAEPS.BankLIst.BankListItems;
import com.pay2all.aeps.PaytmAEPS.DevicesList.DeviceCardAdapter;
import com.pay2all.aeps.PaytmAEPS.DevicesList.DevicesItems;
import com.pay2all.aeps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BalaneEnquiry extends AppCompatActivity {
    String Piddata;
    final int REQUEST = 1421;
    String aadhaar_number;
    String action = "scan";
    public AlertDialog alertDialog;
    String bank_id;
    JSONObject biodata;
    String biometricdata;
    Button button_re_capture;
    Button button_submit;

    DBHelper dbHelper;

    String f138dc;
    String device_package;
    ProgressDialog dialog;
    String dpID;
    EditText edittext_customer_aadhaar_number;
    EditText edittext_customer_mobile;
    String errCode;
    String errInfo;
    String fCount;
    String fType;
    String hmac;
    String iCount;
    ImageView imageview_finger_print;
    LinearLayout ll_fingerprint;
    LinearLayout ll_select_bank;
    LinearLayout ll_select_device;

    String f139mc;

    String f140mi;
    String nmPoints;
    String number;
    String pCount;
    String pType;
    String pidtype;

    String sessionKey;
    String f137ci;
    String qScore;
    String rdsID;
    String rdsVer;
    SecretKey secretKey = null;
    TextView textview_bank;
    TextView textview_capture_quality;
    TextView textview_select_device;
    String token;

    Uri file=null;

    boolean is5secondcall=false;
    String request_id="",aepsreport_id="";
    AlertDialog create=null;

    public BalaneEnquiry() {
        String str = "";
        number = str;
        aadhaar_number = str;
        bank_id = str;
        device_package = str;
        biometricdata = str;
        pidtype = str;
        f137ci = str;
        token = str;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_mini_statement_paytm);

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        edittext_customer_mobile = (EditText) findViewById(R.id.edittext_customer_mobile);
        edittext_customer_aadhaar_number = (EditText) findViewById(R.id.edittext_customer_aadhaar_number);
        ll_select_bank = (LinearLayout) findViewById(R.id.ll_select_bank);
        ll_select_device = (LinearLayout) findViewById(R.id.ll_select_device);
        textview_bank = (TextView) findViewById(R.id.textview_bank);
        textview_select_device = (TextView) findViewById(R.id.textview_select_device);
        ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);
        imageview_finger_print = (ImageView) findViewById(R.id.imageview_finger_print);
        textview_capture_quality = (TextView) findViewById(R.id.textview_capture_quality);
        button_re_capture = (Button) findViewById(R.id.button_re_capture);
        button_submit = (Button) findViewById(R.id.button_submit);
        dbHelper = new DBHelper(this);
        try {
            secretKey = UTLsData.generateKey(dbHelper.mGet());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e2) {
            e2.printStackTrace();
        }
        token = dbHelper.access_token;
        ll_select_bank.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BankListBottomSheet3DialogFragment bankListBottomSheet3DialogFragment = new BankListBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
//                bundle.putString("type", ExifInterface.GPS_MEASUREMENT_2D);
                bundle.putString("activity", "balance paytm");
                bankListBottomSheet3DialogFragment.setArguments(bundle);
                bankListBottomSheet3DialogFragment.show(getSupportFragmentManager(), bankListBottomSheet3DialogFragment.getTag());
            }
        });
        ll_select_device.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mShowDialog();
            }
        });
        button_re_capture.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mCheckAppInstall();
            }
        });
        button_submit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(BalaneEnquiry.this)) {
                    String str = "";
                    if (edittext_customer_mobile.getText().toString().equals(str)) {
                        Toast.makeText(BalaneEnquiry.this, "Please enter customer mobile number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_customer_mobile.getText().toString().length() < 10) {
                        Toast.makeText(BalaneEnquiry.this, "Please enter a valid customer mobile number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_customer_aadhaar_number.getText().toString().equals(str)) {
                        Toast.makeText(BalaneEnquiry.this, "Please enter customer aadhaar number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_customer_aadhaar_number.getText().toString().length() < 12) {
                        Toast.makeText(BalaneEnquiry.this, "Please enter a valid customer aadhaar number", Toast.LENGTH_SHORT).show();
                    } else if (bank_id.equals(str)) {
                        Toast.makeText(BalaneEnquiry.this, "Please select bank", Toast.LENGTH_SHORT).show();
                    } else if (device_package.equals(str)) {
                        Toast.makeText(BalaneEnquiry.this, "Please select device ", Toast.LENGTH_SHORT).show();
                    } else {

                        number = edittext_customer_mobile.getText().toString();
                        aadhaar_number = edittext_customer_aadhaar_number.getText().toString();
                        if (action.equals("scan")) {
                            mCheckAppInstall();
                        } else {
                            mValidateData();
                        }
                    }
                } else {
                    Toast.makeText(BalaneEnquiry.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String mEncodByteToStringBase64(byte[] bArr) {
        return Base64.encodeToString(bArr, 0);
    }

    public void mGetBankDetail(BankListItems bankListItems) {
        textview_bank.setText(bankListItems.getBank_name());
        bank_id = bankListItems.getId();
    }

    public void mGetData(DevicesItems devicesItems) {
        alertDialog.dismiss();
        textview_select_device.setText(devicesItems.getName());
        device_package = devicesItems.getPackage_name();
    }

    public void mShowDialog() {
        View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custome_alert_dialog_show_devices_list_paytm, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageview_close);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        Builder builder = new Builder(this);
        builder.setCancelable(false);
        builder.setView(inflate);
        alertDialog = builder.create();
        String[] strArr = {"Mantra", "Morpho", "Startek","SecuGen","Tatvik","Precision","Evolute"};
        String[] strArr2 = {"MANTRA_PROTOBUF", "MORPHO_PROTOBUF", "STARTEK_PROTOBUF", "SECUGEN_PROTOBUF", "TATVIK_PROTOBUF", "PRECISION_PROTOBUF", "EVOLUTE_PROTOBUF"};
        String[] strArr3 = {"com.mantra.rdservice", "com.scl.rdservice", "com.acpl.registersdk","com.secugen.rdservice","com.tatvik.bio.tmf20","com.precision.pb510.rdservice","com.evolute.rdservice"};
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<DevicesItems> arrayList = new ArrayList<>();
        DeviceCardAdapter deviceCardAdapter = new DeviceCardAdapter(this, arrayList);
        recyclerView.setAdapter(deviceCardAdapter);
        for (int i = 0; i < strArr.length; i++) {
            DevicesItems devicesItems = new DevicesItems();
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("");
            devicesItems.setId(sb.toString());
            devicesItems.setName(strArr[i]);
            devicesItems.setPackage_name(strArr3[i]);
            devicesItems.setType(strArr2[i]);
            devicesItems.setFragment_type("enquiry paytm");
            arrayList.add(devicesItems);
            deviceCardAdapter.notifyDataSetChanged();
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void mCheckAppInstall() {
        String str = "android.intent.action.VIEW";
        if (isPackageInstalled(device_package,getPackageManager())) {
            mGetBioData(device_package);
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(device_package);
            startActivity(new Intent(str, Uri.parse(sb.toString())));
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://play.google.com/store/apps/details?id=");
            sb2.append(device_package);
            startActivity(new Intent(str, Uri.parse(sb2.toString())));
        }
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private final void mGetBioData(String str) {
        try {
            String createPidOptXML = DeviceScanFormate.createPidOptXML("0");

            if (!str.equals("")&&str.equalsIgnoreCase("com.precision.pb510.rdservice"))
            {
                createPidOptXML = DeviceScanFormate.createPrecisionPidOptXML("0");
            }

            if (createPidOptXML != null) {
                Log.e("PidOptions", createPidOptXML);
                Intent intent = new Intent();
                intent.setPackage(str);
                intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent.putExtra("PID_OPTIONS", createPidOptXML);
                startActivityForResult(intent, 1421);
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        String str = "qScore";
        String str2 = "ci";
        String str3 = "errInfo";
        String str4 = "type";
        String str5 = "Skey";
        String str6 = "errCode";
        String str7 = "Data";
        String str8 = "Resp";
        String str9 = "PidData";
        super.onActivityResult(i, i2, intent);
        if (i == 1421 && i2 == -1) {
            biometricdata = intent.getStringExtra("PID_DATA");
            String str10 = "";
            biometricdata = biometricdata.replaceAll("\n", str10);
            biometricdata = biometricdata.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", str10);
            StringBuilder sb = new StringBuilder();
            sb.append("result ");
            sb.append(biometricdata);
            Log.e("bio data", sb.toString());
            try {
                JSONObject json = new XmlToJson.Builder(biometricdata).build().toJson();
                if (json.has(str9)) {
                    JSONObject jSONObject = json.getJSONObject(str9);
                    if (jSONObject.has(str8)) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject(str8);
                        if (jSONObject2.has(str6)) {
                            errCode = jSONObject2.getString(str6);
                        }
                        if (jSONObject2.has(str3)) {
                            errInfo = jSONObject2.getString(str3);
                        }
                        if (jSONObject2.has(str)) {
                            qScore = jSONObject2.getString(str);
                        }
                    }
                    String str11 = "content";
                    if (jSONObject.has(str7)) {
                        JSONObject jSONObject3 = jSONObject.getJSONObject(str7);
                        if (jSONObject3.has(str4)) {
                            pidtype = jSONObject3.getString(str4);
                        }
                        if (jSONObject3.has(str11)) {
                            Piddata = jSONObject3.getString(str11);
                        }
                    }
                    if (jSONObject.has(str5)) {
                        JSONObject jSONObject4 = jSONObject.getJSONObject(str5);
                        if (jSONObject4.has(str2)) {
                            f137ci = jSONObject4.getString(str2);
                        }
                        if (jSONObject4.has(str11)) {
                            sessionKey = jSONObject4.getString(str11);
                        }
                    }
                    if (errCode.equals("0")) {
                        ll_fingerprint.setVisibility(View.VISIBLE);
                        imageview_finger_print.setColorFilter(getResources().getColor(R.color.green));
                        action = "submit";
                        button_submit.setText(getResources().getString(R.string.proceed_now));
                        button_re_capture.setVisibility(View.VISIBLE);
                        TextView textView = textview_capture_quality;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Capture Score ");
                        sb2.append(qScore);
                        sb2.append(" %");
                        textView.setText(sb2.toString());
                        return;
                    }
                    action = "scan";
                    ll_fingerprint.setVisibility(View.GONE);
                    button_re_capture.setVisibility(View.GONE);
                    button_submit.setText(getResources().getString(R.string.capture_fingerprint));
                    Toast.makeText(this, errInfo, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void mValidateData() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("outlet_id",  Constants.outlet_id);
            jSONObject.put("payment_id",  "5");
            jSONObject.put("mobile_number",  Constants.mobile);
            jSONObject.put("customer_mobile_number",  number);
            jSONObject.put("aadhar_number",  aadhaar_number);
            jSONObject.put("bank_id",  bank_id);
            jSONObject.put("amount",  "0");
            jSONObject.put("provider_id",  "159");
            jSONObject.put("api_id", (Object) "51");
            StringBuilder sb = new StringBuilder();
            sb.append(biometricdata);
            sb.append("");
            jSONObject.put("biometric_data",  sb.toString());
            jSONObject.put("PidDatatype",  pidtype);
            jSONObject.put("ci",  f137ci);
            jSONObject.put("client_id",  Constants.mobile);

            Log.e("sending","obj "+jSONObject.toString());
            try {
                mCallService(mEncodByteToStringBase64(UTLsData.encryptMsg(jSONObject.toString(), secretKey)));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e2) {
                e2.printStackTrace();
            } catch (InvalidKeyException e3) {
                e3.printStackTrace();
            } catch (InvalidParameterSpecException e4) {
                e4.printStackTrace();
            } catch (IllegalBlockSizeException e5) {
                e5.printStackTrace();
            } catch (BadPaddingException e6) {
                e6.printStackTrace();
            } catch (UnsupportedEncodingException e7) {
                e7.printStackTrace();
            }
        } catch (JSONException e8) {
            e8.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    protected void mCallService(String str) {

        String sb2 = "https://api.pay2all.in/outlet/v1/smartoutlet";

//        Uri.Builder builder = new Uri.Builder();
//        builder.appendQueryParameter("json_data", str);
//        builder.appendQueryParameter("biometric_data", biometricdata);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("json_data",str)
                .addFormDataPart("biometric_data",biometricdata)
                .build();

        CallResAPIPOSTMethod r2 = new CallResAPIPOSTMethod(this, body, sb2, true, "POST") {
            public void onPreExecute() {
                super.onPreExecute();
                BalaneEnquiry balaneEnquiry = BalaneEnquiry.this;
                balaneEnquiry.dialog = new ProgressDialog(balaneEnquiry);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            public void onPostExecute(String str) {
                super.onPostExecute(str);

                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(str);
                Log.e("data","Response "+sb.toString());
//                mShowReceipt(sb.toString());

                String status_id="",order_id="",message="";

                try {
                    JSONObject jsonObject=new JSONObject(str);

                    if (jsonObject.has("status_id"))
                    {
                        status_id=jsonObject.getString("status_id");
                    }

                    if (jsonObject.has("request_id"))
                    {
                        request_id=jsonObject.getString("request_id");
                    }
                    if (jsonObject.has("aepsreport_id"))
                    {
                        aepsreport_id=jsonObject.getString("aepsreport_id");
                    }

                    if (jsonObject.has("order_id"))
                    {
                        order_id=jsonObject.getString("order_id");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (status_id.equals("2"))
                    {
                        if (Constants.isReceipt) {
                            mShowReceipt(str);
                        }
                        else
                        {
                            Constants.all_data=str;
                            Intent intent=new Intent();
                            intent.putExtra("alldata",Constants.all_data);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                mValidateDataForStatus(request_id, aepsreport_id);

                            }
                        }, 5000);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
        r2.execute();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    public void mShowReceipt(String str) {

        String status_id="";
        try {
            JSONObject jsonObject=new JSONObject(str);
            if (jsonObject.has("status_id"))
            {
                status_id=jsonObject.getString("status_id");

                if (status_id.equals("1")||status_id.equalsIgnoreCase("success")) {
                    action = "scan";
                    ll_fingerprint.setVisibility(View.GONE);
                    button_re_capture.setVisibility(View.GONE);
                    button_submit.setText(getResources().getString(R.string.capture_fingerprint));
                    bank_id = "";
                    device_package = "";
                    edittext_customer_mobile.setText("");
                    edittext_customer_aadhaar_number.setText("");

                    textview_select_device.setText("Select Device");
                    biometricdata = "";

                    textview_bank.setText("Select Bank");
                }

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Intent intent=new Intent(BalaneEnquiry.this, ReceiptPaytm.class);
        intent.putExtra("status",status_id);
        intent.putExtra("data",str);
        intent.putExtra("number",number);
        intent.putExtra("service_name","Balance Enquiry");
        startActivity(intent);


//        boolean z;
//        String str3 = "order_id";
//        String str4 = "utr";
//        String str5 = "balance";
//        String str6 = "message";
//        String str7 = "status_id";
//        View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_alert_dialog_for_balance_enquiry_aeps_paytm, null);
//        TextView textView = (TextView) inflate.findViewById(R.id.textview_available_balance);
//        TextView textView2 = (TextView) inflate.findViewById(R.id.textview_utr);
//        TextView textView3 = (TextView) inflate.findViewById(R.id.textview_order_id);
//        TextView textView4 = (TextView) inflate.findViewById(R.id.textview_customer_mob);
//        TextView textView5 = (TextView) inflate.findViewById(R.id.textview_aadhaar_number);
//        TextView textview_message = (TextView) inflate.findViewById(R.id.textview_message);
//        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageview_close);
//        final LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.ll_all_detail);
//        TextView textView7 = (TextView) inflate.findViewById(R.id.textview_bank);
//
//        TextView textview_service =  inflate.findViewById(R.id.textview_service);
//        TextView textview_amount =  inflate.findViewById(R.id.textview_amount);
//
//        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.imageview_share);
//        imageView2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.S)
//                {
//                    mSaveFile(loadBitmapFromView(linearLayout));
//                    Intent intent = new Intent("android.intent.action.SEND");
//                    intent.setType("image/*");
//                    intent.putExtra("android.intent.extra.STREAM", file);
//                    startActivity(Intent.createChooser(intent, "Share Receipt"));
//                    finish();
//
//                }
//                else {
//                    if (mCheckWriteStorage()) {
//                        mSaveFile(loadBitmapFromView(linearLayout));
//                        Intent intent = new Intent("android.intent.action.SEND");
//                        intent.setType("image/*");
//                        intent.putExtra("android.intent.extra.STREAM", file);
//                        startActivity(Intent.createChooser(intent, "Share Receipt"));
//                        finish();
//                    }
//                    else {
//                        mRequestWriteStorage();
//                    }
//                }
//            }
//        });
//
//        textview_service.setText("Balance Enquiry");
//        String str8 = "";
//        if (!str.equals(str8)) {
//            linearLayout.setVisibility(View.VISIBLE);
//            textview_message.setVisibility(View.VISIBLE);
//            try {
//                JSONObject jSONObject = new JSONObject(str);
////                String response_data=finaljSONObject.getString("response");
//
////                if (response_data.equals(""))
////                {
////                    if (!is5secondcall) {
////                        new Handler().postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////                                is5secondcall=true;
////                                if (create!=null) {
////                                    create.dismiss();
////                                }
////                                mValidateDataForStatus(request_id, aepsreport_id);
////
////                            }
////                        }, 5000);
////                    }
////                }
////                JSONObject jSONObject=new JSONObject(response_data);
//
//
//                String string = jSONObject.has(str7) ? jSONObject.getString(str7) : str8;
//                String string2 = jSONObject.has(str6) ? jSONObject.getString(str6) : str8;
//                String string3 = jSONObject.has(str5) ? jSONObject.getString(str5) : str8;
//                String string4 = jSONObject.has(str4) ? jSONObject.getString(str4) : str8;
//                String string5 = jSONObject.has(str3) ? jSONObject.getString(str3) : str8;
//
//                if (jSONObject.has("amount"))
//                {
//                    String amount=jSONObject.getString("amount");
//                    textview_amount.setText("Rs "+amount);
//                }
//                else
//                {
//                    textview_amount.setText("Rs 0.0");
//                }
//
//
//                linearLayout.setVisibility(View.VISIBLE);
//                textview_message.setVisibility(View.VISIBLE);
//                textView2.setText(string4);
//                textView3.setText(string5);
//                StringBuilder sb = new StringBuilder();
//                sb.append("Rs ");
//                sb.append(string3);
//                textView.setText(sb.toString());
//                textView4.setText(number);
//                StringBuilder sb2 = new StringBuilder();
//                sb2.append("XXXX-XXXX-");
//                sb2.append(aadhaar_number.substring(8, aadhaar_number.length()));
//                textView5.setText(sb2.toString());
//                textView7.setText(textview_bank.getText().toString());
//
//
//                if (string.equals(str8)) {
//                    imageView2.setVisibility(View.GONE);
//                    textview_message.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.GONE);
//                    textview_message.setText(string2);
//                } else if (string.equals("1")||string.equalsIgnoreCase("success")) {
//                    action = "scan";
//                    ll_fingerprint.setVisibility(View.GONE);
//                    button_re_capture.setVisibility(View.GONE);
//                    button_submit.setText(getResources().getString(R.string.capture_fingerprint));
//                    bank_id = str8;
//                    device_package = str8;
//                    edittext_customer_mobile.setText(str8);
//                    edittext_customer_aadhaar_number.setText(str8);
//
//                    textview_select_device.setText("Select Device");
//                    biometricdata = str8;
//
//                    textview_bank.setText("Select Bank");
//                } else {
//                    imageView2.setVisibility(View.GONE);
//                    textview_message.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.GONE);
//                    if (!string2.equals(str8)) {
//                        textview_message.setText(string2);
//                    } else {
//                        textview_message.setText("Something went wrong please try again later");
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            z = false;
//        } else {
//            imageView2.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.GONE);
//            z = false;
//            textview_message.setVisibility(View.VISIBLE);
//            textview_message.setText("Server not responsing, please try again later...");
//        }
//        Builder builder = new Builder(this);
//        builder.setCancelable(z);
//        builder.setView(inflate);
//          create = builder.create();
//        imageView.setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                create.dismiss();
//            }
//        });
//        create.show();
    }

    public void mValidateDataForStatus(String request_id,String aepsreport_id) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("outlet_id",  Constants.outlet_id);
            jSONObject.put("payment_id",  "12");
            jSONObject.put("mobile_number",  Constants.mobile);
            jSONObject.put("customer_mobile_number",  number);
            jSONObject.put("aadhar_number",  aadhaar_number);
            jSONObject.put("bank_id",  bank_id);
            jSONObject.put("amount",  "0");
            jSONObject.put("provider_id",  "159");
            jSONObject.put("api_id",  "51");
            jSONObject.put("request_id", request_id );
            jSONObject.put("aepsreport_id", aepsreport_id );
            StringBuilder sb = new StringBuilder();
            sb.append(biometricdata);
            sb.append("");
//            jSONObject.put("biometric_data",  sb.toString());
//            jSONObject.put("PidDatatype",  pidtype);
//            jSONObject.put("ci",  f137ci);
            jSONObject.put("client_id",  Constants.mobile);

            Log.e("sending","obj "+jSONObject.toString());
            try {
                mCallServiceForStatus(mEncodByteToStringBase64(UTLsData.encryptMsg(jSONObject.toString(), secretKey)));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e2) {
                e2.printStackTrace();
            } catch (InvalidKeyException e3) {
                e3.printStackTrace();
            } catch (InvalidParameterSpecException e4) {
                e4.printStackTrace();
            } catch (IllegalBlockSizeException e5) {
                e5.printStackTrace();
            } catch (BadPaddingException e6) {
                e6.printStackTrace();
            } catch (UnsupportedEncodingException e7) {
                e7.printStackTrace();
            }
        } catch (JSONException e8) {
            e8.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mCallServiceForStatus(String str) {

        String sb2 = "https://api.pay2all.in/outlet/v1/smartoutlet";


//        Uri.Builder builder = new Uri.Builder();
//        builder.appendQueryParameter("json_data", str);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("json_data",str)
                .build();


        CallResAPIPOSTMethod r2 = new CallResAPIPOSTMethod(this, body, sb2, true, "POST") {
            public void onPreExecute() {
                super.onPreExecute();
                BalaneEnquiry balaneEnquiry = BalaneEnquiry.this;
                balaneEnquiry.dialog = new ProgressDialog(balaneEnquiry);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            public void onPostExecute(String str) {
                super.onPostExecute(str);
                dialog.dismiss();
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(str);
                Log.e("data","Response "+sb.toString());

                if (Constants.isReceipt) {
                    mShowReceipt(sb.toString());
                }else
                {
                    Constants.all_data=str;
                    Intent intent=new Intent();
                    intent.putExtra("alldata",Constants.all_data);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        };
        r2.execute();
    }

    public static Bitmap loadBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
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

    public void mRequestWriteStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }
}
