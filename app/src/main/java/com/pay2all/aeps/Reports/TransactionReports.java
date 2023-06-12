package com.pay2all.aeps.Reports;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.pay2all.aeps.Constants;
import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.DetectConnection;
import com.pay2all.aeps.R;
import com.pay2all.aeps.UTLsData;

import org.json.JSONArray;
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
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TransactionReports extends AppCompatActivity {

    SwipeRefreshLayout swiprefresh_transaction_reports;
    RecyclerView recyclerview_transaction_reports;
    RecyclerView.LayoutManager layoutManager;
    List<TransactionReportsItem> transactionReportsItems;
    TransactionReportsCardAdapter transactionReportsCardAdapter=null;
    TextView textview_message;

    DBHelper dbHelper;
    SecretKey secretKey=null;

    int page=0;

    public static boolean last_array_empty=false;

    boolean swiped_refresh=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_reports);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper=new DBHelper(TransactionReports.this);

        try
        {
            secretKey=UTLsData.generateKey(dbHelper.mGet());
        }
        catch (NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }



        recyclerview_transaction_reports=findViewById(R.id.recyclerview_transaction_reports);
        recyclerview_transaction_reports.setHasFixedSize(true);
        recyclerview_transaction_reports.setLayoutManager(new LinearLayoutManager(this));
        transactionReportsItems=new ArrayList<>();
        transactionReportsCardAdapter=new TransactionReportsCardAdapter(this,transactionReportsItems);
        recyclerview_transaction_reports.setAdapter(transactionReportsCardAdapter);

        swiprefresh_transaction_reports=findViewById(R.id.swiprefresh_transaction_reports);
        textview_message=findViewById(R.id.textview_message);
        swiprefresh_transaction_reports.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swiprefresh_transaction_reports.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(TransactionReports.this))
                {
                    textview_message.setVisibility(View.GONE);
                    recyclerview_transaction_reports.setVisibility(View.VISIBLE);

                    try
                    {
                        page=0;

                        swiped_refresh=true;

                        transactionReportsCardAdapter.notifyDataSetChanged();
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("outlet_id", Constants.outlet_id);
                        jsonObject.put("mobile_number", Constants.mobile);
                        jsonObject.put("payment_id", "1");
                        jsonObject.put("page", page+"");

                        try {

                            String data=mEncodByteToStringBase64(UTLsData.encryptMsg(jsonObject.toString(), secretKey));

                            Log.e("data",data);
                            mGetUserdetail(data);
                        }
                        catch (NoSuchAlgorithmException e)
                        {
                            e.printStackTrace();
                        }
                        catch (NoSuchPaddingException e)
                        {
                            e.printStackTrace();
                        }
                        catch (InvalidKeyException e)
                        {
                            e.printStackTrace();
                        }
                        catch (InvalidParameterSpecException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalBlockSizeException e)
                        {
                            e.printStackTrace();
                        }
                        catch (BadPaddingException e)
                        {
                            e.printStackTrace();
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(TransactionReports.this, R.string.no_enternet_connection, Toast.LENGTH_SHORT).show();
                    textview_message.setText("No internet connection");
                    textview_message.setVisibility(View.VISIBLE);
                    recyclerview_transaction_reports.setVisibility(View.GONE);
                    swiprefresh_transaction_reports.setRefreshing(false);
                }
            }
        });

        if (DetectConnection.checkInternetConnection(TransactionReports.this))
        {
            try
            {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("outlet_id", Constants.outlet_id);
                jsonObject.put("mobile_number", Constants.mobile);
                jsonObject.put("payment_id", "1");
                jsonObject.put("page", page+"");

                try {

                    String data=mEncodByteToStringBase64(UTLsData.encryptMsg(jsonObject.toString(), secretKey));

                    Log.e("data",data);
                    mGetUserdetail(data);
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
                catch (NoSuchPaddingException e)
                {
                    e.printStackTrace();
                }
                catch (InvalidKeyException e)
                {
                    e.printStackTrace();
                }
                catch (InvalidParameterSpecException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalBlockSizeException e)
                {
                    e.printStackTrace();
                }
                catch (BadPaddingException e)
                {
                    e.printStackTrace();
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            textview_message.setVisibility(View.GONE);
            recyclerview_transaction_reports.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(TransactionReports.this, R.string.no_enternet_connection, Toast.LENGTH_SHORT).show();
            textview_message.setText("No internet connection");
            textview_message.setVisibility(View.VISIBLE);
            recyclerview_transaction_reports.setVisibility(View.GONE);
        }

    }


    //    to get user details and refresh balance
    private  void  mGetUserdetail(final String data) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                swiprefresh_transaction_reports.setRefreshing(true);
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
                Log.e("user detail",result);

                if (swiprefresh_transaction_reports.isRefreshing())
                {
                    swiprefresh_transaction_reports.setRefreshing(false);
                }

                if (!result.equals(""))
                {
                    mShowTransactionReports(result);
                }
                else
                {
                }

            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }
    protected void mShowTransactionReports(final String json)
    {

         String id="";
         String date="";
         String provider="";
         String number="";
         String txnid="";
         String amount="";
         String commisson="";
         String total_balance="";
         String status="";
        try
        {
            JSONObject jsonObject=new JSONObject(json);

            if (swiped_refresh) {
                transactionReportsItems.clear();
            }

            if (jsonObject.has("reports")) {
                JSONArray jsonArray = jsonObject.getJSONArray("reports");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        id=data.getString("id");
                        date=data.getString("date");
                        provider=data.getString("provider");
                        number=data.getString("number");
                        txnid=data.getString("txnid");
                        amount=data.getString("amount");
                        commisson=data.getString("commisson");
                        total_balance=data.getString("total_balance");
                        status=data.getString("status");


                        TransactionReportsItem items=new TransactionReportsItem();
                        items.setId(id);
                        items.setDate(date);
                        items.setProvider(provider);
                        items.setNumber(number);
                        items.setTxnid(txnid);
                        items.setAmount(amount);
                        items.setCommisson(commisson);
                        items.setTotal_balance(total_balance);
                        items.setStatus(status);

                        transactionReportsItems.add(items);
                        transactionReportsCardAdapter.notifyDataSetChanged();

                    }

                }

                if (jsonArray.length()!=0)
                {
                    last_array_empty=false;
                }
                else
                {
                    last_array_empty=true;
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // for encode data in base64
    protected String mEncodByteToStringBase64(byte[] value)
    {
        String base64 = Base64.encodeToString(value, Base64.DEFAULT);

        return base64;
    }

    public void mGetNextPageData()
    {
        page+=1;

        if (DetectConnection.checkInternetConnection(TransactionReports.this))
        {
            try
            {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("outlet_id", Constants.outlet_id);
                jsonObject.put("mobile_number", Constants.mobile);
                jsonObject.put("payment_id", "1");
                jsonObject.put("page", page+"");

                try {

                    String data=mEncodByteToStringBase64(UTLsData.encryptMsg(jsonObject.toString(), secretKey));

                    Log.e("data",data);
                    mGetUserdetail(data);
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }
                catch (NoSuchPaddingException e)
                {
                    e.printStackTrace();
                }
                catch (InvalidKeyException e)
                {
                    e.printStackTrace();
                }
                catch (InvalidParameterSpecException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalBlockSizeException e)
                {
                    e.printStackTrace();
                }
                catch (BadPaddingException e)
                {
                    e.printStackTrace();
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            textview_message.setVisibility(View.GONE);
            recyclerview_transaction_reports.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (transactionReportsItems!=null) {
                    List<TransactionReportsItem> temp = new ArrayList();
                    for (TransactionReportsItem d : transactionReportsItems) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getNumber().contains(newText) || d.getProvider().contains(newText) || d.getNumber().toLowerCase().contains(newText) ||
                                d.getDate().contains(newText) || d.getAmount().contains(newText)) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    transactionReportsCardAdapter.UpdateList(temp);
                }
                return true;
            }

        });
    }



}