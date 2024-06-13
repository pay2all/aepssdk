package com.pay2all.aeps.InsuranceDetails

import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pay2all.aeps.Constants
import com.pay2all.aeps.CustomeProgressBar
import com.pay2all.aeps.DBHelper
import com.pay2all.aeps.R
import com.pay2all.aeps.UTLsData
import org.json.JSONException
import org.json.JSONObject
import javax.crypto.SecretKey

class Insurance : AppCompatActivity() {
    lateinit var wv_insurance : WebView
    lateinit var viewModel: InsuranceViewModel

    lateinit var secretKey: SecretKey
    lateinit var tv_message: TextView

    lateinit var dbHelper : DBHelper
    var dialog: CustomeProgressBar?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay2all_insurance_service)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DBHelper(this)

        dialog=CustomeProgressBar(this@Insurance)
        tv_message=findViewById(R.id.tv_message)
        wv_insurance=findViewById(R.id.wv_insurance)
        wv_insurance.settings.javaScriptEnabled=true
//        wv_insurance.setWebChromeClient(object : WebChromeClient()
//        {
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                super.onProgressChanged(view, newProgress)
//                if (newProgress==100)
//                {
//                    dialog?.mDismissDialog()
//                }
//            }
//        })

        wv_insurance.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

            }
        })

        viewModel=ViewModelProvider(this).get(InsuranceViewModel::class.java)
        viewModel.liveData.observe(this@Insurance)
        {
            dialog?.mDismissDialog()
            println("insurance response $it")

            var status_id=""
            var redirect_url=""
            var message=""
            try {
                val jsonObject=JSONObject(it)

                if (jsonObject.has("message"))
                {
                    message=jsonObject.getString("message");
                }

                if (jsonObject.has("redirect_url"))
                {
                    redirect_url=jsonObject.getString("redirect_url")
                    wv_insurance.loadUrl(redirect_url)
                }
                else if (message.isNotEmpty())
                {
                    tv_message.text=message
                    wv_insurance.visibility=View.GONE
                    tv_message.visibility=View.VISIBLE
                }
            }
            catch (e:JSONException)
            {
                dialog?.mDismissDialog()
                e.printStackTrace()
                Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }
//        wv_insurance.loadUrl("https://www.google.com")

        secretKey = UTLsData.generateKey(this.dbHelper.mGet());
        val json_data=mEncodByteToStringBase64(UTLsData.encryptMsg(mGetJsonData(), this.secretKey))
        println("sending data insurance $json_data")

        dialog?.mShowDialog()
        viewModel.mGetData(Constants.mobile,json_data!!)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
        {
            if (wv_insurance.canGoBack())
            {
                wv_insurance.goBack()
            }
            else {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun mGetJsonData() : String
    {
        val jsonobject = JSONObject()
        jsonobject.put("mobile_number",Constants.mobile)
        return jsonobject.toString()
    }


    fun mEncodByteToStringBase64(bArr: ByteArray?): String? {
        return Base64.encodeToString(bArr, 0)
    }

    override fun onBackPressed() {
        if (wv_insurance.canGoBack())
        {
            wv_insurance.goBack()
        }
        else {
            super.onBackPressed()
        }
    }
}