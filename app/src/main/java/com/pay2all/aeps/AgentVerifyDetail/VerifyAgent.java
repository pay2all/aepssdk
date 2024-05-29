package com.pay2all.aeps.AgentVerifyDetail;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pay2all.aeps.AEPSICICI.DeviceScanFormateNew;
import com.pay2all.aeps.BuildConfig;
import com.pay2all.aeps.Constants;
import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.DetectConnection;
import com.pay2all.aeps.R;
import com.pay2all.aeps.UTLsData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerifyAgent extends AppCompatActivity implements LocationListener {

    EditText ed_name;
    EditText ed_mobile_number;
    EditText ed_aadhaar_number;
    Button bt_verify;

    SecretKey secretKey ;

    String provider_id = "159";

    DBHelper dbHelper;

    RelativeLayout rl_device;

    TextView tv_device;

    String biometricdata;

    String ci = "";
    String device_package;
    String errCode = "";
    String errInfo = "";
    ImageView imageview_finger_print;
    LinearLayout ll_fingerprint;
    String pidtype = "";
    String qScore = "";
    String sessionKey = "";
    String Piddata = "";

     Button button_re_capture;
    TextView textview_capture_quality;

    String action = "scan";

    AlertDialog alertDialog;

    MyLocation myLocation = new MyLocation();

    double lat=0,log=0;

    LocationManager locationManager;

    String provider="";

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    RadioGroup radioGroup;

    ProgressDialog dialog;

    String aadhaar="0";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_verify);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().hasExtra("aadhaar_verify"))
        {
            aadhaar=getIntent().getStringExtra("aadhaar_verify");
        }


        radioGroup=findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i==R.id.rb_aeps)
            {
                provider_id="159";
            }
            else {
                provider_id="175";
            }
        });


        if (aadhaar.equals("1"))
        {
            RadioButton rb_aadhaar= findViewById(R.id.rb_aadhaar);
            rb_aadhaar.setChecked(true);
            provider_id="175";
        }


        dbHelper =new DBHelper(VerifyAgent.this);

        ed_name = findViewById(R.id.ed_name);
        ed_name.setText(Constants.name);

        ed_mobile_number = findViewById(R.id.ed_mobile_number);
        ed_mobile_number.setText(Constants.mobile);


        ed_aadhaar_number = findViewById(R.id.ed_aadhaar_number);
        ed_aadhaar_number.setText(Constants.aadhaar);

        imageview_finger_print = findViewById(R.id.imageview_finger_print);
        ll_fingerprint = findViewById(R.id.ll_fingerprint);

        button_re_capture = findViewById(R.id.button_re_capture);
        button_re_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckAppInstall();
            }
        });

        textview_capture_quality = findViewById(R.id.textview_capture_quality);

        rl_device = findViewById(R.id.rl_device);
        rl_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowDialog();
            }
        }); 

        tv_device = findViewById(R.id.tv_device);
        bt_verify = findViewById(R.id.bt_verify);

//        CONFIG_AGENT_NMAE = dbHelper.mGetFirstName() + " " + dbHelper.mGetLastName()

        try {
            this.secretKey = UTLsData.generateKey(this.dbHelper.mGet());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e2) {
            e2.printStackTrace();
        }


        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(VerifyAgent.this))
                {
                    if (ed_name.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please enter full name",Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_mobile_number.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please enter mobile number",Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_mobile_number.getText().toString().length()<10)
                    {
                        Toast.makeText(getApplicationContext(),"Mobile number should be valid",Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_aadhaar_number.getText().toString().equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please enter aadhaar number",Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_aadhaar_number.getText().toString().length()<12)
                    {
                        Toast.makeText(getApplicationContext(),"Aadhaar number should be valid",Toast.LENGTH_SHORT).show();
                    }
                    else if (device_package.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please select fingerprint device",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (action.equals("scan"))
                        {
                            mCheckAppInstall();
                        }
                        else {

                            try {

                                JSONObject jsonObject =new JSONObject();
                                jsonObject.put("outlet_id", Constants.outlet_id);
                                jsonObject.put("mobile_number", Constants.mobile);
                                jsonObject.put("aadhar_number", ed_aadhaar_number.getText().toString());
//                    jsonObject.put("payment_id", "3")
                                jsonObject.put("payment_id", "9");
                                jsonObject.put("amount", "0");
                                jsonObject.put("provider_id", provider_id);
                                jsonObject.put("biometric_data", biometricdata);
                                jsonObject.put("lat", lat+"");
                                jsonObject.put("long", log+"");

                                try {

                                    Log.e("sending","data "+jsonObject);
                                    String data = mEncodByteToStringBase64(UTLsData.encryptMsg(jsonObject.toString(), secretKey));

                                    mOkkHttps(data);
//                                    mGetOrderId(data);
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (InvalidParameterSpecException e) {
                                    e.printStackTrace();
                                } catch (IllegalBlockSizeException e) {
                                    e.printStackTrace();
                                } catch (BadPaddingException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!hasPermissions(VerifyAgent.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(VerifyAgent.this, PERMISSIONS, PERMISSION_ALL);
                }
                else {
                    boolean r = myLocation.getLocation(getApplicationContext(),
                            locationResult);
                    if (r)
                    {
                        Log.e("location","found");
                    }
                    else
                    {
                        Log.e("location","Not found");
                    }
                }
            }
        },700);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void mGetOrderId(final String data) {



        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialog=new ProgressDialog(VerifyAgent.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);

            }
            @Override
            protected String doInBackground(String... strings) {

                BufferedReader reader;
                StringBuffer buffer;
                String res = null;

                try {
                    URL url = new URL(dbHelper.mBaseURL()+"v1/outletapi");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(40000);
                    con.setConnectTimeout(40000);
                    con.setRequestMethod("POST");
//                    con.setRequestProperty("Content-Type", "application/json");

//                    paramaters in header
                    con.setRequestProperty("Authorization","Bearer ");
                    con.setRequestProperty("Accept","application/json");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    con.setRequestProperty("mobile_number",number);
//                    con.setRequestProperty();
                    con.setDoInput(true);
                    con.setDoOutput(true);

//                    parameter in body
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("json_data", data)
                            .appendQueryParameter("biometric_data", biometricdata)
                            ;
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();


                    int status = con.getResponseCode();
                    InputStream inputStream;
                    if (status == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    } else {
                        inputStream = con.getErrorStream();
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    res = buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                Log.e("payout response",">>>"+result);

                dialog.dismiss();

                if (!result.equals(""))
                {
//                    Intent intent=new Intent(VerifyAgent.this, Receipt.class);
//                    intent.putExtra("data",result);
//                    startActivity(intent);
//                    finish();
//                    Toast.makeText(VerifyAgent.this, result, Toast.LENGTH_SHORT).show();


                    String status="",message="";
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        if (jsonObject.has("status_id"))
                        {
                            status=jsonObject.getString("status_id");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        mShowStatus(status,message);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(VerifyAgent.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }

            }
        }

        getJSONData jsd =new getJSONData();
        jsd.execute();
    }

    protected void mOkkHttps(String json_data)
    {
        class DatNewSubmit extends AsyncTask<String, String,String>
        {
            public void onPreExecute() {
                super.onPreExecute();

                dialog = new ProgressDialog(VerifyAgent.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected String doInBackground(String... strings) {

                String response_data=null;
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()

                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)

                            .build();

                    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                            .setType(Objects.requireNonNull(mediaType))

                            .addFormDataPart("json_data",json_data)
                            .addFormDataPart("biometric_data",biometricdata)
                            .build();
                    Request request = new Request.Builder()
//                            .url(dbHelper.mBaseURL()+"v1/outletapi")
                            .url(BuildConfig.BASEURL+"api/outlet/v1/outletapi")
                            .method("POST", body)
                            .addHeader("Content-Type","application/json; charset=utf-8")
                            .addHeader("Accept","application/json")
                            .build();
                    Response response = client.newCall(request).execute();

                    response_data= response.body().string();

                    Log.e("respon","respos "+response.message());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    response_data=e.getMessage();
                }

                return response_data;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                Log.e("response","data "+result);

                if (!result.equals(""))
                {
//                    Intent intent=new Intent(VerifyAgent.this, Receipt.class);
//                    intent.putExtra("data",result);
//                    startActivity(intent);
//                    finish();
//                    Toast.makeText(VerifyAgent.this, result, Toast.LENGTH_SHORT).show();

                    String status="",message="";
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        if (jsonObject.has("status_id"))
                        {
                            status=jsonObject.getString("status_id");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        mShowStatus(status,message);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        new DatNewSubmit().execute();
    }

    public String mEncodByteToStringBase64(byte[] bArr) {
        return Base64.encodeToString(bArr, 0);
    }

    public void mGetData(FingerDevicesItems devicesItems) {
        this.alertDialog.dismiss();
        this.tv_device.setText(devicesItems.getName());
        this.device_package = devicesItems.getPackage_name();
    }

    public void mShowDialog() {
        View inflate = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custome_alert_dialog_show_devices_list, null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.imageview_close);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(inflate);
        this.alertDialog = builder.create();
        String[] strArr = {"Mantra","Mantra L1","Mantra IRIS", "Morpho","Morpho (IDEMIA) L1", "Startek", "SecuGen", "Tatvik", "Precision","Precision (PB1000) L1","Aratek A600","Evolute"};
        String[] strArr3 = {"com.mantra.rdservice",
                "com.mantra.mfs110.rdservice",
                "com.mantra.mis100v2.rdservice",
                "com.scl.rdservice",
                "com.idemia.l1rdservice",
                "com.acpl.registersdk",
                "com.secugen.rdservice",
                "com.tatvik.bio.tmf20",
                "com.precision.pb510.rdservice",
                "in.co.precisionit.innaitaadhaar",
                "co.aratek.asix_gms.rdservice",
                "com.evolute.rdservice"};
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList arrayList = new ArrayList();
        FingerDeviceCardAdapter deviceCardAdapter = new FingerDeviceCardAdapter(this, arrayList);
        recyclerView.setAdapter(deviceCardAdapter);
        for (int i = 0; i < strArr.length; i++) {
            FingerDevicesItems devicesItems = new FingerDevicesItems();
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("");
            devicesItems.setId(sb.toString());
            devicesItems.setName(strArr[i]);
            devicesItems.setPackage_name(strArr3[i]);
            devicesItems.setType("");
            devicesItems.setFragment_type("with");
            arrayList.add(devicesItems);
            deviceCardAdapter.notifyDataSetChanged();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alertDialog.dismiss();
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
                            this.ci = jSONObject4.getString(str2);
                        }
                        if (jSONObject4.has(str11)) {
                            this.sessionKey = jSONObject4.getString(str11);
                        }
                    }
                    if (this.errCode.equals("0")) {
                        this.ll_fingerprint.setVisibility(View.VISIBLE);
                        this.imageview_finger_print.setColorFilter(getResources().getColor(R.color.green));
                        this.action = "submit";
                        this.bt_verify.setText(getResources().getString(R.string.proceed_now));
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
                    this.bt_verify.setText(getResources().getString(R.string.capture_fingerprint));
                    Toast.makeText(this, this.errInfo, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    protected void mShowStatus(final String status, String message)
    {
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater.inflate(R.layout.custom_alertdalog_for_message,null);

       TextView textview_ok=v2.findViewById(R.id.button_ok);
        TextView textview_message=(TextView)v2.findViewById(R.id.textview_message);
        if (!message.equals("")) {
            textview_message.setText(message);
        }
        else
        {
            textview_message.setText("Something went wrong...");
        }
       ImageView imageview_messase_image=(ImageView)v2.findViewById(R.id.imageview_messase_image);


        final AlertDialog.Builder builder2=new AlertDialog.Builder(VerifyAgent.this);
        builder2.setCancelable(false);

        if (status.equals("1")||status.equalsIgnoreCase("true"))
        {
            textview_message.setText(message);
            imageview_messase_image.setImageResource(R.drawable.success);
        }
        else if (status.equalsIgnoreCase("false")||status.equals("2"))
        {
            textview_message.setTextColor(getResources().getColor(R.color.red));
            imageview_messase_image.setImageResource(R.drawable.failure_icon);
        }
        else
        {
            textview_message.setTextColor(getResources().getColor(R.color.red));
            imageview_messase_image.setImageResource(R.drawable.failure_icon);
        }

        builder2.setView(v2);
        final AlertDialog alert=builder2.create();

        textview_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                if (status.equals("1")||status.equalsIgnoreCase("true")) {


//                to send broadcast
                    Intent intent = new Intent("agent_verification");
                    intent.putExtra("status", status);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                    finish();
                }
            }
        });

        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.e("data","lat long \nlat "+location.getLatitude()+"\nlong "+location.getLongitude());
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    0);
        }
        LocationListener.super.onProviderDisabled(provider);
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                        "Your GPS seems like disabled, Please enable to get live location")
                .setCancelable(false).setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog,
                                                final int id) {
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    protected void mShowLocation()
    {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    0);
        }

        locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        statusCheck();

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
            // Get the location from the given provider
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 500, 0, this);

            if (location != null)
                onLocationChanged(location);
            else
                location = locationManager.getLastKnownLocation(provider);
            if (location != null)
                onLocationChanged(location);
            else

                Toast.makeText(getBaseContext(), "Location can't be retrieved",
                        Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {
            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();

//            Toast.makeText(getApplicationContext(), "Got Location",
//                    Toast.LENGTH_LONG).show();

            lat=Latitude;
            log=Longitude;

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    TextView tv_lat_log=findViewById(R.id.tv_lat_log);
                    tv_lat_log.setText("Latitude : "+lat+", Longitude : "+log);
                }
            });


        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        boolean r = myLocation.getLocation(getApplicationContext(),
                locationResult);

        if (r)
        {
            Log.e("location","found");
        }
        else
        {
            Log.e("location","Not found");
        }
    }

    @Override
    protected void onResume() {
        mShowLocation();
        super.onResume();
    }
}