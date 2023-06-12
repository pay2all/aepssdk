package com.pay2all.aeps.PaytmAEPS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MiniStatement extends AppCompatActivity {
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

    /* renamed from: ci */
    String f142ci;
    DBHelper dbHelper;

    /* renamed from: dc */
    String f143dc;
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

    /* renamed from: mc */
    String f144mc;

    /* renamed from: mi */
    String f145mi;
    String nmPoints;
    String number;
    String pCount;
    String pType;
    String pidtype;
    String qScore;
    String rdsID;
    String rdsVer;
    SecretKey secretKey = null;
    String sessionKey;
    TextView textview_bank;
    TextView textview_capture_quality;
    TextView textview_select_device;
    String token;


    boolean is5secondcall=false;
    String request_id="",aepsreport_id="";
    AlertDialog create=null;

    public MiniStatement() {
        String str = "";
        this.number = str;
        this.aadhaar_number = str;
        this.bank_id = str;
        this.device_package = str;
        this.biometricdata = str;
        this.pidtype = str;
        this.f142ci = str;
        this.token = str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_mini_statement_paytm);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.edittext_customer_mobile = (EditText) findViewById(R.id.edittext_customer_mobile);
        this.edittext_customer_aadhaar_number = (EditText) findViewById(R.id.edittext_customer_aadhaar_number);
        this.ll_select_bank = (LinearLayout) findViewById(R.id.ll_select_bank);
        this.ll_select_device = (LinearLayout) findViewById(R.id.ll_select_device);
        this.textview_bank = (TextView) findViewById(R.id.textview_bank);
        this.textview_select_device = (TextView) findViewById(R.id.textview_select_device);
        this.ll_fingerprint = (LinearLayout) findViewById(R.id.ll_fingerprint);
        this.imageview_finger_print = (ImageView) findViewById(R.id.imageview_finger_print);
        this.textview_capture_quality = (TextView) findViewById(R.id.textview_capture_quality);
        this.button_re_capture = (Button) findViewById(R.id.button_re_capture);
        this.button_submit = (Button) findViewById(R.id.button_submit);
        this.dbHelper = new DBHelper(this);
        try {
            this.secretKey = UTLsData.generateKey(this.dbHelper.mGet());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e2) {
            e2.printStackTrace();
        }
        this.token = this.dbHelper.access_token;
        this.ll_select_bank.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BankListBottomSheet3DialogFragment bankListBottomSheet3DialogFragment = new BankListBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
//                bundle.putString("type", ExifInterface.GPS_MEASUREMENT_2D);
                bundle.putString("activity", "mini paytm");
                bankListBottomSheet3DialogFragment.setArguments(bundle);
                bankListBottomSheet3DialogFragment.show(MiniStatement.this.getSupportFragmentManager(), bankListBottomSheet3DialogFragment.getTag());
            }
        });
        this.ll_select_device.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MiniStatement.this.mShowDialog();
            }
        });
        this.button_re_capture.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MiniStatement.this.mCheckAppInstall();
            }
        });
        this.button_submit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(MiniStatement.this)) {
                    String str = "";
                    if (MiniStatement.this.edittext_customer_mobile.getText().toString().equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please enter customer mobile number", Toast.LENGTH_SHORT).show();
                    } else if (MiniStatement.this.edittext_customer_mobile.getText().toString().length() < 10) {
                        Toast.makeText(MiniStatement.this, "Please enter a valid customer mobile number", Toast.LENGTH_SHORT).show();
                    } else if (MiniStatement.this.edittext_customer_aadhaar_number.getText().toString().equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please enter customer aadhaar number", Toast.LENGTH_SHORT).show();
                    } else if (MiniStatement.this.edittext_customer_aadhaar_number.getText().toString().length() < 12) {
                        Toast.makeText(MiniStatement.this, "Please enter a valid customer aadhaar number", Toast.LENGTH_SHORT).show();
                    } else if (MiniStatement.this.bank_id.equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please select bank", Toast.LENGTH_SHORT).show();
                    } else if (MiniStatement.this.device_package.equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please select device ", Toast.LENGTH_SHORT).show();
                    } else {
                        MiniStatement miniStatement = MiniStatement.this;
                        miniStatement.number = miniStatement.edittext_customer_mobile.getText().toString();
                        MiniStatement miniStatement2 = MiniStatement.this;
                        miniStatement2.aadhaar_number = miniStatement2.edittext_customer_aadhaar_number.getText().toString();
                        if (MiniStatement.this.action.equals("scan")) {
                            MiniStatement.this.mCheckAppInstall();
                        } else {
                            MiniStatement.this.mValidateData();
                        }
                    }
                } else {
                    Toast.makeText(MiniStatement.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String mEncodByteToStringBase64(byte[] bArr) {
        return Base64.encodeToString(bArr, 0);
    }

    public void mGetBankDetail(BankListItems bankListItems) {
        this.textview_bank.setText(bankListItems.getBank_name());
        this.bank_id = bankListItems.getId();
    }

    public void mGetData(DevicesItems devicesItems) {
        this.alertDialog.dismiss();
        this.textview_select_device.setText(devicesItems.getName());
        this.device_package = devicesItems.getPackage_name();
    }

    /* access modifiers changed from: protected */
    public void mShowDialog() {
        View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custome_alert_dialog_show_devices_list_paytm, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageview_close);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        Builder builder = new Builder(this);
        builder.setCancelable(false);
        builder.setView(inflate);
        this.alertDialog = builder.create();
        String[] strArr = {"Mantra", "Morpho", "Startek","SecuGen","Tatvik","Precision","Evolute"};
        String[] strArr2 = {"MANTRA_PROTOBUF", "MORPHO_PROTOBUF", "STARTEK_PROTOBUF", "SECUGEN_PROTOBUF", "TATVIK_PROTOBUF", "PRECISION_PROTOBUF", "EVOLUTE_PROTOBUF"};
        String[] strArr3 = {"com.mantra.rdservice", "com.scl.rdservice", "com.acpl.registersdk","com.secugen.rdservice","com.tatvik.bio.tmf20","com.precision.pb510.rdservice","com.evolute.rdservice"};
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList = new ArrayList();
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
            devicesItems.setFragment_type("mini paytm");
            arrayList.add(devicesItems);
            deviceCardAdapter.notifyDataSetChanged();
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MiniStatement.this.alertDialog.dismiss();
            }
        });
        this.alertDialog.show();
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
            this.biometricdata = intent.getStringExtra("PID_DATA");
            String str10 = "";
            this.biometricdata = this.biometricdata.replaceAll("\n", str10);
            this.biometricdata = this.biometricdata.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", str10);
            StringBuilder sb = new StringBuilder();
            sb.append("result ");
            sb.append(this.biometricdata);
            Log.e("bio data", sb.toString());
            try {
                JSONObject json = new XmlToJson.Builder(this.biometricdata).build().toJson();
                if (json.has(str9)) {
                    JSONObject jSONObject = json.getJSONObject(str9);
                    if (jSONObject.has(str8)) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject(str8);
                        if (jSONObject2.has(str6)) {
                            this.errCode = jSONObject2.getString(str6);
                        }
                        if (jSONObject2.has(str3)) {
                            this.errInfo = jSONObject2.getString(str3);
                        }
                        if (jSONObject2.has(str)) {
                            this.qScore = jSONObject2.getString(str);
                        }
                    }
                    String str11 = "content";
                    if (jSONObject.has(str7)) {
                        JSONObject jSONObject3 = jSONObject.getJSONObject(str7);
                        if (jSONObject3.has(str4)) {
                            this.pidtype = jSONObject3.getString(str4);
                        }
                        if (jSONObject3.has(str11)) {
                            this.Piddata = jSONObject3.getString(str11);
                        }
                    }
                    if (jSONObject.has(str5)) {
                        JSONObject jSONObject4 = jSONObject.getJSONObject(str5);
                        if (jSONObject4.has(str2)) {
                            this.f142ci = jSONObject4.getString(str2);
                        }
                        if (jSONObject4.has(str11)) {
                            this.sessionKey = jSONObject4.getString(str11);
                        }
                    }
                    if (this.errCode.equals("0")) {
                        this.ll_fingerprint.setVisibility(View.VISIBLE);
                        this.imageview_finger_print.setColorFilter(getResources().getColor(R.color.green));
                        this.action = "submit";
                        this.button_submit.setText(getResources().getString(R.string.proceed_now));
                        this.button_re_capture.setVisibility(View.VISIBLE);
                        TextView textView = this.textview_capture_quality;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Capture Score ");
                        sb2.append(this.qScore);
                        sb2.append(" %");
                        textView.setText(sb2.toString());
                        return;
                    }
                    this.action = "scan";
                    this.ll_fingerprint.setVisibility(View.GONE);
                    this.button_re_capture.setVisibility(View.GONE);
                    this.button_submit.setText(getResources().getString(R.string.capture_fingerprint));
                    Toast.makeText(this, this.errInfo, Toast.LENGTH_SHORT).show();
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
            jSONObject.put("customer_mobile_number",  this.number);
            jSONObject.put("aadhar_number",  this.aadhaar_number);
            jSONObject.put("bank_id",  this.bank_id);
            jSONObject.put("amount",  "0");
            jSONObject.put("provider_id",  "172");
            jSONObject.put("api_id", (Object) "51");
            StringBuilder sb = new StringBuilder();
            sb.append(this.biometricdata);
            sb.append("");
            jSONObject.put("biometric_data",  sb.toString());
            jSONObject.put("PidDatatype",  this.pidtype);
            jSONObject.put("ci",  this.f142ci);
            jSONObject.put("client_id",  Constants.mobile);
            try {
                mCallService(mEncodByteToStringBase64(UTLsData.encryptMsg(jSONObject.toString(), this.secretKey)));
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

    private void mCallService(String str) {
        String sb2 = "https://api.pay2all.in/outlet/v1/smartoutlet";

//
//        Uri.Builder builder = new Uri.Builder();
//        builder.appendQueryParameter("json_data", str);
//        builder.appendQueryParameter("biometric_data", this.biometricdata);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("json_data",str)
                .addFormDataPart("biometric_data",biometricdata)
                .build();

        @SuppressLint("StaticFieldLeak")
        CallResAPIPOSTMethod r2 = new CallResAPIPOSTMethod(this, body, sb2, true, "POST") {

            public void onPreExecute() {
                super.onPreExecute();
                MiniStatement miniStatement = MiniStatement.this;
                miniStatement.dialog = new ProgressDialog(miniStatement);
                MiniStatement.this.dialog.setMessage("Please wait...");
                MiniStatement.this.dialog.show();
                MiniStatement.this.dialog.setCancelable(false);
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
                        if (!message.equals(""))
                        {
                            Toast.makeText(MiniStatement.this, message, Toast.LENGTH_SHORT).show();
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

    public void mValidateDataForStatus(String request_id,String aepsreport_id) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("outlet_id",  dbHelper.mGetOutLetId());
            jSONObject.put("payment_id",  "12");
            jSONObject.put("mobile_number",  dbHelper.mGetMobile());
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
            jSONObject.put("client_id",  dbHelper.mGetMobile());

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
                MiniStatement balaneEnquiry = MiniStatement.this;
                balaneEnquiry.dialog = new ProgressDialog(balaneEnquiry);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            public void onPostExecute(String str) {
                super.onPostExecute(str);
                dialog.dismiss();

                Log.e("data","Response "+ str);

                String str2 = "message";
                String str3 = "status_id";
                MiniStatement.this.dialog.dismiss();
                StringBuilder sb = new StringBuilder();
                sb.append("data : ");
                sb.append(str);
                Log.e("response", sb.toString());
                String str4 = "";

                if (!Constants.isReceipt)
                {
                    Constants.all_data=str;
                    Intent intent=new Intent();
                    intent.putExtra("alldata",Constants.all_data);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {

                    if (!str.equals(str4)) {
                        try {
                            JSONObject jSONObject = new JSONObject(str);

//                        String response=finaljSONObject.getString("response");

//                        if (response.equals(""))
//                        {
//                            if (!is5secondcall) {
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        is5secondcall=true;
//                                        if (create!=null) {
//                                            create.dismiss();
//                                        }
//                                        mValidateDataForStatus(request_id, aepsreport_id);
//
//                                    }
//                                }, 5000);
//                            }
//                        }


//                        JSONObject jSONObject=new JSONObject(response);

                            String string = jSONObject.has(str3) ? jSONObject.getString(str3) : str4;
                            String string2 = jSONObject.has(str2) ? jSONObject.getString(str2) : str4;
                            if (string.equals("1") || string.equalsIgnoreCase("success")) {
                                MiniStatement.this.action = "scan";
                                MiniStatement.this.ll_fingerprint.setVisibility(View.GONE);
                                MiniStatement.this.button_re_capture.setVisibility(View.GONE);
                                MiniStatement.this.button_submit.setText(MiniStatement.this.getResources().getString(R.string.capture_fingerprint));
                                MiniStatement.this.bank_id = str4;
                                MiniStatement.this.device_package = str4;
                                MiniStatement.this.edittext_customer_mobile.setText(str4);
                                MiniStatement.this.edittext_customer_aadhaar_number.setText(str4);
                                MiniStatement.this.textview_bank.setText("Select Bank");
                                MiniStatement.this.textview_select_device.setText("Select Device");
                                MiniStatement.this.biometricdata = str4;
                                Intent intent = new Intent(MiniStatement.this, MiniStatementLIst.class);
                                intent.putExtra("data", str);
                                intent.putExtra("number", MiniStatement.this.number);
                                intent.putExtra("aadhaar", MiniStatement.this.aadhaar_number);
                                MiniStatement.this.startActivity(intent);
                                return;
                            }
                            View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_alertdalog_for_message_paytm, null);
                            TextView textView = (TextView) inflate.findViewById(R.id.button_ok);
                            TextView textView2 = (TextView) inflate.findViewById(R.id.textview_message);
                            textView2.setText(string2);
                            textView2.setTextColor(Color.parseColor("#6c6c6c"));
                            ImageView imageView = (ImageView) inflate.findViewById(R.id.imageview_messase_image);
                            imageView.setImageResource(R.drawable.error_icon);
                            imageView.setVisibility(View.GONE);
                            Builder builder = new Builder(MiniStatement.this);
                            builder.setCancelable(false);
                            builder.setView(inflate);
                            create = builder.create();
                            textView.setOnClickListener(new OnClickListener() {
                                public void onClick(View view) {
                                    create.dismiss();
                                }
                            });
                            create.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MiniStatement.this, "Server not responding", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        };
        r2.execute();
    }
}
