package com.pay2all.aeps.PaytmAEPS.BankLIst;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pay2all.aeps.Constants;
import com.pay2all.aeps.DBHelper;
import com.pay2all.aeps.PaytmAEPS.DetectConnection;
import com.pay2all.aeps.PaytmAEPS.UTLsData;
import com.pay2all.aeps.R;

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


/**
 * Created by Basant on 3/29/2018.
 */

public class BankListBottomSheet3DialogFragment extends BottomSheetDialogFragment {

    RecyclerView recyclerview_operator;
    EditText edittext_search;

    public static String activity_name;




    private BottomSheetBehavior mBehavior;

    ImageView imageview_back_icon;

    public static BankListBottomSheet3DialogFragment dialogFragment;

    List<BankListItems> bankListItems;
    ListOfAllBanksCardAdapter listOfAllBanksCardAdapter;


    DBHelper dbHelper;
    SecretKey secretKey=null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.bottom_sheet_layout_bank_list_paytm, null);

        dialogFragment=this;

        dbHelper=new DBHelper(getActivity());

        try
        {
            secretKey= UTLsData.generateKey(dbHelper.mGet());
        }
        catch (NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        linearLayout.setLayoutParams(params);

        imageview_back_icon=(ImageView)view.findViewById(R.id.btnclose);
        imageview_back_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        edittext_search=view.findViewById(R.id.edittext_search);
        edittext_search.setVisibility(View.VISIBLE);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (bankListItems!=null) {
                    List<BankListItems> temp = new ArrayList();
                    for (BankListItems d : bankListItems) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getIfsc().toLowerCase().contains(editable.toString().toLowerCase()) || d.getBank_name().toLowerCase().contains(editable.toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    listOfAllBanksCardAdapter.UpdateList(temp);
                }
            }
        });

        activity_name=getArguments().getString("activity");


        recyclerview_operator = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerview_operator.setHasFixedSize(true);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(getContext()));

        bankListItems=new ArrayList<>();

        listOfAllBanksCardAdapter=new ListOfAllBanksCardAdapter(getContext(),bankListItems);
        recyclerview_operator.setAdapter(listOfAllBanksCardAdapter);

        if (DetectConnection.checkInternetConnection(getActivity()))
        {
            try
            {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("outlet_id", Constants.outlet_id);
                jsonObject.put("mobile_number", Constants.mobile);
                jsonObject.put("payment_id", "5");
                jsonObject.put("type", "4");

                try {

                    String data=mEncodByteToStringBase64(UTLsData.encryptMsg(jsonObject.toString(), secretKey));

                    Log.e("data",data);
                    mGetBanksList(data);
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

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    private  void  mGetBanksList(final String data) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

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
                Log.e("bank list",result);

                if (!result.equals(""))
                {
                    mShowBankList(result);
                }
                else
                {
                }

            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }

    protected String mEncodByteToStringBase64(byte[] value)
    {
        String base64 = Base64.encodeToString(value, Base64.DEFAULT);

        return base64;
    }

    protected void mShowBankList(final String myjson)
    {
        try {
            JSONArray jsonArray=new JSONArray(myjson);
            bankListItems.clear();
            edittext_search.setVisibility(View.VISIBLE);
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);
                BankListItems items=new BankListItems();
                items.setId(data.getString("id"));
                items.setBank_name(data.getString("bank_name"));
                items.setIfsc(data.getString("ifsc"));
                items.setBank_code(data.getString("bank_code"));
                items.setStatus(data.getString("status"));
                items.setIs_imps(data.getString("is_imps"));

                if (data.has("icon"))
                {
                    items.setIcon(data.getString("icon"));
                }
                else
                {
                    items.setIcon("");
                }

                bankListItems.add(items);
                listOfAllBanksCardAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}