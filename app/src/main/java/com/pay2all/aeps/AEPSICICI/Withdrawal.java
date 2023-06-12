package com.pay2all.aeps.AEPSICICI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.google.android.material.textfield.TextInputLayout;
import com.pay2all.aeps.AEPSICICI.BankLIst.BankListBottomSheet3DialogFragment;
import com.pay2all.aeps.AEPSICICI.BankLIst.BankListItems;
import com.pay2all.aeps.Constants;
import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.DetectConnection;
import com.pay2all.aeps.AEPSICICI.DevicesList.DeviceCardAdapter;
import com.pay2all.aeps.AEPSICICI.DevicesList.DevicesItems;
import com.pay2all.aeps.PaytmAEPS.ReceiptPaytm;
import com.pay2all.aeps.R;
import com.pay2all.aeps.UTLsData;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Withdrawal extends AppCompatActivity {
    String Piddata;
    final int REQUEST = 1421;
    String aadhaar_number;
    String action = "scan";
    public AlertDialog alertDialog;
    String amount;
    String bank_id;
    String biometricdata;
    Button button_re_capture;
    Button button_submit;

    /* renamed from: ci */
    String f146ci;
    DBHelper dbHelper;
    String device_package;
    ProgressDialog dialog;
    EditText edittext_amount;
    EditText edittext_customer_aadhaar_number;
    EditText edittext_customer_mobile;
    String errCode;
    String errInfo;
    ImageView imageview_finger_print;
    LinearLayout ll_fingerprint;
    LinearLayout ll_select_bank;
    LinearLayout ll_select_device;
    String number;
    String pidtype;
    String qScore;
    SecretKey secretKey = null;
    String sessionKey;
    TextInputLayout textinputlayout_amount;
    TextView textview_bank;
    TextView textview_capture_quality;
    TextView textview_select_device;
    String token;

    public Withdrawal() {
        String str = "";
        this.number = str;
        this.aadhaar_number = str;
        this.bank_id = str;
        this.device_package = str;
        this.biometricdata = str;
        this.pidtype = str;
        this.f146ci = str;
        this.token = str;
    }

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_mini_statement);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        sharedPreferences=getSharedPreferences("last_aeps_details",0);
        editor=sharedPreferences.edit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.textinputlayout_amount = (TextInputLayout) findViewById(R.id.textinputlayout_amount);
        this.textinputlayout_amount.setVisibility(0);
        this.edittext_customer_mobile = (EditText) findViewById(R.id.edittext_customer_mobile);
        this.edittext_amount = (EditText) findViewById(R.id.edittext_amount);
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
                bundle.putString("activity", "with");
                bankListBottomSheet3DialogFragment.setArguments(bundle);
                bankListBottomSheet3DialogFragment.show(getSupportFragmentManager(), bankListBottomSheet3DialogFragment.getTag());
            }
        });
        this.ll_select_device.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mShowDialog();
            }
        });
        this.button_re_capture.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mCheckAppInstall();
            }
        });
        this.button_submit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(Withdrawal.this)) {
                    String str = "";
                    if (edittext_customer_mobile.getText().toString().equals(str)) {
                        Toast.makeText(Withdrawal.this, "Please enter customer mobile number", 0).show();
                    } else if (edittext_customer_mobile.getText().toString().length() < 10) {
                        Toast.makeText(Withdrawal.this, "Please enter a valid customer mobile number", 0).show();
                    } else if (edittext_customer_aadhaar_number.getText().toString().equals(str)) {
                        Toast.makeText(Withdrawal.this, "Please enter customer aadhaar number", 0).show();
                    } else if (edittext_customer_aadhaar_number.getText().toString().length() < 12) {
                        Toast.makeText(Withdrawal.this, "Please enter a valid customer aadhaar number", 0).show();
                    } else if (edittext_amount.getText().toString().equals(str)) {
                        Toast.makeText(Withdrawal.this, "Please enter amount", 0).show();
                    } else if (Integer.parseInt(edittext_amount.getText().toString()) < 10) {
                        Toast.makeText(Withdrawal.this, "Please enter a valid amount", 0).show();
                    } else if (bank_id.equals(str)) {
                        Toast.makeText(Withdrawal.this, "Please select bank", 0).show();
                    } else if (device_package.equals(str)) {
                        Toast.makeText(Withdrawal.this, "Please select device ", 0).show();
                    } else {
                        Withdrawal withdrawal = Withdrawal.this;
                        withdrawal.number = withdrawal.edittext_customer_mobile.getText().toString();
                        Withdrawal withdrawal2 = Withdrawal.this;
                        withdrawal2.aadhaar_number = withdrawal2.edittext_customer_aadhaar_number.getText().toString();
                        Withdrawal withdrawal3 = Withdrawal.this;
                        withdrawal3.amount = withdrawal3.edittext_amount.getText().toString();
                        if (action.equals("scan")) {
                            mCheckAppInstall();
                        }
                        else {
                            mValidateData();
                        }
                    }
                }
                else {
                    Toast.makeText(Withdrawal.this, "No internet connection", 0).show();
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

    public void mShowDialog() {
        View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custome_alert_dialog_show_devices_list, null);
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
            devicesItems.setFragment_type("with");
            arrayList.add(devicesItems);
            deviceCardAdapter.notifyDataSetChanged();
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        this.alertDialog.show();
    }


    public void mCheckAppInstall() {
        String str = "android.intent.action.VIEW";
        if (isAppInstalled(this, this.device_package)) {
            mGetBioData(this.device_package);
            return;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(this.device_package);
            startActivity(new Intent(str, Uri.parse(sb.toString())));
        } catch (ActivityNotFoundException unused) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("https://play.google.com/store/apps/details?id=");
            sb2.append(this.device_package);
            startActivity(new Intent(str, Uri.parse(sb2.toString())));
        }
    }

    public static boolean isAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }

    private void mGetBioData(String package_name) {
        try {
            String pIDOptions = DeviceScanFormateNew.createPidOptXML("0");

            if (package_name.equalsIgnoreCase("com.precision.pb510.rdservice")) {
                pIDOptions = DeviceScanFormateNew.createPrecisionPidOptXML("0");
            }
            else if (package_name.equalsIgnoreCase("com.secugen.rdservice"))
            {
                pIDOptions = DeviceScanFormateNew.createSecugenPidXML("0");
//                Toast.makeText(this, "selected package "+package_name+"\nSecugen Block Executed", Toast.LENGTH_SHORT).show();
            }
            else if (package_name.equalsIgnoreCase("com.acpl.registersdk"))
            {
                pIDOptions = DeviceScanFormateNew.createPidOptXMLOld("0");
//                Toast.makeText(this, "selected package "+package_name+"\nSecugen Block Executed", Toast.LENGTH_SHORT).show();
            }

            editor=sharedPreferences.edit().putString("scan_formate", pIDOptions);
            editor=sharedPreferences.edit().putString("scan_formate_package", package_name);
            editor.commit();

            if (pIDOptions != null) {
                Log.e("PidOptions", pIDOptions);
                Intent intent = new Intent();
                intent.setPackage(package_name);
                intent.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent.putExtra("PID_OPTIONS", pIDOptions);
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
                            this.f146ci = jSONObject4.getString(str2);
                        }
                        if (jSONObject4.has(str11)) {
                            this.sessionKey = jSONObject4.getString(str11);
                        }
                    }
                    if (this.errCode.equals("0")) {
                        this.ll_fingerprint.setVisibility(0);
                        this.imageview_finger_print.setColorFilter(getResources().getColor(R.color.green));
                        this.action = "submit";
                        this.button_submit.setText(getResources().getString(R.string.proceed_now));
                        this.button_re_capture.setVisibility(0);
                        TextView textView = this.textview_capture_quality;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Capture Score ");
                        sb2.append(this.qScore);
                        sb2.append(" %");
                        textView.setText(sb2.toString());
                        return;
                    }
                    this.action = "scan";
                    this.ll_fingerprint.setVisibility(8);
                    this.button_re_capture.setVisibility(8);
                    this.button_submit.setText(getResources().getString(R.string.capture_fingerprint));
                    Toast.makeText(this, this.errInfo, 0).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void mValidateData() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("outlet_id", (Object) Constants.outlet_id);
            jSONObject.put("payment_id", (Object) "5");
            jSONObject.put("mobile_number", (Object) Constants.mobile);
            jSONObject.put("customer_mobile_number", (Object) this.number);
            jSONObject.put("aadhar_number", (Object) this.aadhaar_number);
            jSONObject.put("bank_id", (Object) this.bank_id);
            jSONObject.put("amount", (Object) this.amount);
            jSONObject.put("provider_id", (Object) "158");
            StringBuilder sb = new StringBuilder();
            sb.append(this.biometricdata);
            sb.append("");
            jSONObject.put("biometric_data", (Object) sb.toString());
            jSONObject.put("PidDatatype", (Object) this.pidtype);
            jSONObject.put("ci", (Object) this.f146ci);
            jSONObject.put("client_id", (Object) Constants.mobile);
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

    @SuppressLint("StaticFieldLeak")
    private void mCallService(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dbHelper.mBaseURL());
        sb.append("v1/outletaeps");
        String sb2 = sb.toString();


//        Uri.Builder builder = new Uri.Builder();
//        builder.appendQueryParameter("json_data", str);
//        builder.appendQueryParameter("biometric_data", this.biometricdata);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                            .setType(Objects.requireNonNull(mediaType))

                .addFormDataPart("json_data",str)
                .addFormDataPart("biometric_data",biometricdata)
                .build();

         new CallResAPIPOSTMethodOkkHttp(this, body, sb2, true, "POST") {
            public void onPreExecute() {
                super.onPreExecute();
                Withdrawal withdrawal = Withdrawal.this;
                withdrawal.dialog = new ProgressDialog(withdrawal);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            public void onPostExecute(String str) {
                super.onPostExecute(str);
                dialog.dismiss();
                Log.e("data","withdrawal "+str);
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(str);


                if (!str.equals("")) {

                    try{
                        String str8="";
                        JSONObject jsonObject=new JSONObject(str);
                        if (jsonObject.has("status"))
                        {
                            if (jsonObject.getString("status").equalsIgnoreCase("0"))
                            {


                                action = "scan";
                                ll_fingerprint.setVisibility(View.GONE);
                                button_re_capture.setVisibility(View.GONE);
                                button_submit.setText(getResources().getString(R.string.capture_fingerprint));
                                bank_id = str8;
                                device_package = str8;
                                edittext_customer_mobile.setText(str8);
                                edittext_customer_aadhaar_number.setText(str8);
                                textview_bank.setText("Select Bank");
                                textview_select_device.setText("Select Device");
                                biometricdata = str8;
                            }


                            if (!Constants.isReceipt)
                            {
                                Constants.all_data=str;
                                Intent intent=new Intent();
                                intent.putExtra("alldata",Constants.all_data);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(Withdrawal.this, ReceiptTest.class);
                                intent.putExtra("data", str);
                                intent.putExtra("bank", textview_bank.getText().toString());
                                intent.putExtra("number", number);
                                intent.putExtra("aadhaar", aadhaar_number);
                                intent.putExtra("service", "Cash Withdrawal");

                                startActivity(intent);
                            }
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                } else {

                    editor.putString("last_response",str);
                    editor.commit();

                    mShowReceipt(sb.toString());
                }
            }
        }.execute();
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

        Intent intent=new Intent(Withdrawal.this, ReceiptICICI.class);
        intent.putExtra("status",status_id);
        intent.putExtra("data",str);
        intent.putExtra("number",number);
        intent.putExtra("service_name","Cash Withdrawal");
        startActivity(intent);


//        boolean z;
//        String str2 = str;
//        String str3 = "order_id";
//        String str4 = "utr";
//        String str5 = "balance";
//        String str6 = "message";
//        String amount="";
//        TextView textview_agent_id,textview_bc_name,textview_amount;
//
//
//        String str7 = NotificationCompat.CATEGORY_STATUS;
//        View inflate = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_alert_dialog_for_balance_enquiry, null);
//        TextView textView = (TextView) inflate.findViewById(R.id.textview_available_balance);
//        TextView textView2 = (TextView) inflate.findViewById(R.id.textview_utr);
//        TextView textView3 = (TextView) inflate.findViewById(R.id.textview_order_id);
//        TextView textView4 = (TextView) inflate.findViewById(R.id.textview_customer_mob);
//        TextView textView5 = (TextView) inflate.findViewById(R.id.textview_aadhaar_number);
//        TextView textView6 = (TextView) inflate.findViewById(R.id.textview_message);
//        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageview_close);
//        textview_agent_id=inflate.findViewById(R.id.textview_agent_id);
//        textview_bc_name=inflate.findViewById(R.id.textview_bc_name);
//        textview_amount=inflate.findViewById(R.id.textview_amount);
//        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.ll_all_detail);
//        TextView textView7 = (TextView) inflate.findViewById(R.id.textview_bank);
//        ImageView imageView2 = (ImageView) inflate.findViewById(R.id.imageview_share);
//        View view = inflate;
//        String str8 = "";
//        ImageView imageView3 = imageView2;
//        TextView textView8 = textView5;
//        if (!str2.equals(str8)) {
//            linearLayout.setVisibility(0);
//            textView6.setVisibility(8);
//            try {
//                JSONObject jSONObject = new JSONObject(str2);
//                String string = jSONObject.has(str7) ? jSONObject.getString(str7) : str8;
//                String string2 = jSONObject.has(str6) ? jSONObject.getString(str6) : str8;
//                String string3 = jSONObject.has(str5) ? jSONObject.getString(str5) : str8;
//                String string4 = jSONObject.has(str4) ? jSONObject.getString(str4) : str8;
//                String string5 = jSONObject.has(str3) ? jSONObject.getString(str3) : str8;
//                if (jSONObject.has("amount")){
//                    amount=jSONObject.getString("amount");
//
//                }
//                if (string.equals(str8)) {
//                    imageView3.setVisibility(8);
//                    textView6.setVisibility(0);
//                    linearLayout.setVisibility(8);
//                    textView6.setText(string2);
//                } else if (string.equals("0")) {
//                    this.action = "scan";
//                    this.ll_fingerprint.setVisibility(8);
//                    this.button_re_capture.setVisibility(8);
//                    this.button_submit.setText(getResources().getString(R.string.capture_fingerprint));
//                    this.bank_id = str8;
//                    this.device_package = str8;
//                    this.edittext_customer_mobile.setText(str8);
//                    this.edittext_customer_aadhaar_number.setText(str8);
//                    textView7.setText(this.textview_bank.getText().toString());
//                    this.textview_bank.setText("Select Bank");
//                    this.textview_select_device.setText("Select Device");
//                    this.biometricdata = str8;
//                    textview_amount.setText("Rs "+amount);
//                    textview_agent_id.setText(Constants.outlet_id);
//                    textview_bc_name.setText(Constants.name);
//                    linearLayout.setVisibility(0);
//                    textView6.setVisibility(8);
//                    textView2.setText(string4);
//                    textView3.setText(string5);
//                    StringBuilder sb = new StringBuilder();
//                    sb.append("Rs ");
//                    sb.append(string3);
//                    textView.setText(sb.toString());
//                    textView4.setText(this.number);
//                    StringBuilder sb2 = new StringBuilder();
//                    sb2.append("XXXX-XXXX-");
//                    sb2.append(this.aadhaar_number.substring(8, this.aadhaar_number.length()));
//
//                    textView8.setText(sb2.toString());
//                } else {
//                    imageView3.setVisibility(8);
//                    textView6.setVisibility(0);
//                    linearLayout.setVisibility(8);
//                    if (!string2.equals(str8)) {
//                        textView6.setText(string2);
//                    } else {
//                        textView6.setText("Something went wrong please try again later");
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            z = false;
//        }
//        else {
//            imageView3.setVisibility(8);
//            linearLayout.setVisibility(8);
//            z = false;
//            textView6.setVisibility(0);
//            textView6.setText("Server not responsing, please try again later...");
//        }
//        Builder builder = new Builder(this);
//        builder.setCancelable(z);
//        builder.setView(view);
//        final AlertDialog create = builder.create();
//        imageView.setOnClickListener(new OnClickListener() {
//            public void onClick(View view) {
//                create.dismiss();
//            }
//        });
//        create.show();
    }
}