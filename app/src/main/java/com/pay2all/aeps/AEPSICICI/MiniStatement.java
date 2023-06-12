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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import com.pay2all.aeps.AEPSICICI.BankLIst.BankListBottomSheet3DialogFragment;
import com.pay2all.aeps.AEPSICICI.BankLIst.BankListItems;
import com.pay2all.aeps.Constants;
import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.DetectConnection;
import com.pay2all.aeps.AEPSICICI.DevicesList.DeviceCardAdapter;
import com.pay2all.aeps.AEPSICICI.DevicesList.DevicesItems;
import com.pay2all.aeps.AEPSICICI.MiniStateListData.MiniStatementLIst;
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

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    
    EditText ed_response;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_mini_statement);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences=getSharedPreferences("last_aeps_details",0);
        editor=sharedPreferences.edit();

        ed_response=findViewById(R.id.ed_response);

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
                bundle.putString("activity", "mini");
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
                if (DetectConnection.checkInternetConnection(MiniStatement.this)) {
                    String str = "";
                    if (edittext_customer_mobile.getText().toString().equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please enter customer mobile number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_customer_mobile.getText().toString().length() < 10) {
                        Toast.makeText(MiniStatement.this, "Please enter a valid customer mobile number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_customer_aadhaar_number.getText().toString().equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please enter customer aadhaar number", Toast.LENGTH_SHORT).show();
                    } else if (edittext_customer_aadhaar_number.getText().toString().length() < 12) {
                        Toast.makeText(MiniStatement.this, "Please enter a valid customer aadhaar number", Toast.LENGTH_SHORT).show();
                    } else if (bank_id.equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please select bank", Toast.LENGTH_SHORT).show();
                    } else if (device_package.equals(str)) {
                        Toast.makeText(MiniStatement.this, "Please select device ", Toast.LENGTH_SHORT).show();
                    } else {
                        MiniStatement miniStatement = MiniStatement.this;
                        miniStatement.number = miniStatement.edittext_customer_mobile.getText().toString();
                        MiniStatement miniStatement2 = MiniStatement.this;
                        miniStatement2.aadhaar_number = miniStatement2.edittext_customer_aadhaar_number.getText().toString();
                        if (action.equals("scan")) {
                            mCheckAppInstall();
                        } else {
                            mValidateData();
                        }
                    }
                } else {
                    Toast.makeText(MiniStatement.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
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
        View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custome_alert_dialog_show_devices_list, null);
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
            devicesItems.setFragment_type("mini");
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

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: protected */
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
            jSONObject.put("outlet_id", (Object) Constants.outlet_id);
            jSONObject.put("payment_id", (Object) "5");
            jSONObject.put("mobile_number", (Object) Constants.mobile);
            jSONObject.put("customer_mobile_number", (Object) this.number);
            jSONObject.put("aadhar_number", (Object) this.aadhaar_number);
            jSONObject.put("bank_id", (Object) this.bank_id);
            jSONObject.put("amount", (Object) "0");
            jSONObject.put("provider_id", (Object) "172");
            StringBuilder sb = new StringBuilder();
            sb.append(this.biometricdata);
            sb.append("");
            jSONObject.put("biometric_data", (Object) sb.toString());
            jSONObject.put("PidDatatype", (Object) this.pidtype);
            jSONObject.put("ci", (Object) this.f142ci);
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
                MiniStatement miniStatement = MiniStatement.this;
                miniStatement.dialog = new ProgressDialog(miniStatement);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }


            public void onPostExecute(String str) {

                String str2 = "message";
                String str3 = NotificationCompat.CATEGORY_STATUS;
                super.onPostExecute(str);
                dialog.dismiss();


                editor.putString("last_response",str);
                editor.commit();

                ed_response.setText(str);
//                ed_response.setVisibility(View.VISIBLE);

                StringBuilder sb = new StringBuilder();
                sb.append("data : ");
                sb.append(str);
                Log.e("data","mini "+str);
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
                            String string = jSONObject.has(str3) ? jSONObject.getString(str3) : str4;
                            String string2 = jSONObject.has(str2) ? jSONObject.getString(str2) : str4;
                            if (string.equals("0")) {
                                action = "scan";
                                ll_fingerprint.setVisibility(View.GONE);
                                button_re_capture.setVisibility(View.GONE);
                                button_submit.setText(getResources().getString(R.string.capture_fingerprint));
                                bank_id = str4;
                                device_package = str4;
                                edittext_customer_mobile.setText(str4);
                                edittext_customer_aadhaar_number.setText(str4);
                                textview_bank.setText("Select Bank");
                                textview_select_device.setText("Select Device");
                                biometricdata = str4;
                                Intent intent = new Intent(MiniStatement.this, MiniStatementLIst.class);
                                intent.putExtra("data", str);
                                intent.putExtra("number", number);
                                intent.putExtra("aadhaar", aadhaar_number);
                                startActivity(intent);
                                return;
                            }
                            View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_alertdalog_for_message, null);
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
                            final AlertDialog create = builder.create();
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
        }.execute();

    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }
}
