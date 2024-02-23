package com.pay2all.aeps.aepsnew

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pay2all.aeps.BuildConfig
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class AEPSViewModel constructor(val mBaseURL:String) : ViewModel() {

    private val _mResponse=MutableLiveData<String?>()
    val mResponse:LiveData<String?>
        get() = _mResponse

    private val apiInterface=Retrofit.Builder()
        .baseUrl(mBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIInterface::class.java)

    fun mCallAEPSAPI(biometric_data:String,json_data:String)
    {
//        Log.e("base", "baseurl here we are sending $mBaseURL")
//        viewModelScope.launch{
//            try {
//                val multiPartBody=MultipartBody.Builder().setType(MultipartBody.FORM)
//                multiPartBody.addFormDataPart("biometric_data",biometric_data)
//                multiPartBody.addFormDataPart("json_data",json_data)
//                val data=apiInterface.mCallAEPSService(AEPSDataModel(biometric_data, json_data))
//                if (data.isSuccessful&&data.body()!=null)
//                {
//                    _mResponse.postValue(data.body())
//                }
//                else
//                {
//                    _mResponse.postValue(null)
//                }
//            }
//            catch (e:Exception)
//            {
//                e.printStackTrace()
//                _mResponse.postValue(null)
//            }
//        }


        val endpointURL=BuildConfig.END_POINT_URL


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
                            .addFormDataPart("json_data", json_data)
                            .addFormDataPart("biometric_data", biometric_data)
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
                _mResponse.postValue(s)
            }
        }

        DatNewSubmit().execute()
    }
}