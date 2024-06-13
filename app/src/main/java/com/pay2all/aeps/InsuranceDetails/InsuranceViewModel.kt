package com.pay2all.aeps.InsuranceDetails

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pay2all.aeps.BuildConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class InsuranceViewModel : ViewModel() {

    private val mutableLiveData = MutableLiveData<String?>()
    val liveData: LiveData<String?>
        get() = mutableLiveData

    fun mGetData(mobile_number : String,json_data : String)
    {
        viewModelScope.launch {
            val mBaseURL=BuildConfig.BASEURL
            val endpointURL="api/outlet/v1/insurance/encrypt"
            class DatNewSubmit : AsyncTask<String?, String?, String?>() {

                override fun doInBackground(vararg strings: String?): String? {
                    var response_data: String? = null
                    try {
                        val client = OkHttpClient().newBuilder()
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .writeTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .build()
                        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM) //                            .setType(Objects.requireNonNull(mediaType))
                            .addFormDataPart("mobile_number", mobile_number)
                            .addFormDataPart("json_data", json_data)
                            .build()
                        val request: Request = Request.Builder()
                            .url(mBaseURL+endpointURL)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json; charset=utf-8")
                            .addHeader("Accept", "application/json")
                            .build()
                        val response = client.newCall(request).execute()
                        response_data = response.body!!.string()
//                    Log.e("respon", "respos " + response.message)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        response_data = e.message
                    }
                    return response_data!!
                }

                override fun onPostExecute(s: String?) {
                    super.onPostExecute(s)
                    mutableLiveData.postValue(s)
                }
            }

            DatNewSubmit().execute()
        }
    }

}