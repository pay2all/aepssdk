package com.pay2all.aeps.PaytmAEPS;

import android.app.Activity;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class CallResAPIPOSTMethod extends AsyncTask<String, String, String> {
    RequestBody builder;
    boolean isbodyavaialbel;
    WeakReference<Activity> mWeakActivity;
    String method;
    HttpURLConnection urlConnection;
    String weburl = "";

    public CallResAPIPOSTMethod(Activity activity, RequestBody builder2, String str, boolean z, String str2) {
        this.mWeakActivity = new WeakReference<>(activity);
        this.builder = builder2;
        this.weburl = str;
        this.isbodyavaialbel = z;
        this.method = str2;
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

            Request request = new Request.Builder()
                    .url(weburl)
                    .method("POST", builder)
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
    }}
