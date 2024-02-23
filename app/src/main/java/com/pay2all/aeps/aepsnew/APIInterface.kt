package com.pay2all.aeps.aepsnew

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {

//    @Multipart
//    @FormUrlEncoded
    @POST("api/aeps/outlet/transaction")
    @Headers("Accept:application/json", "Content-Type:application/json; charset=utf-8")
    suspend fun mCallAEPSService(@Body datamodel:AEPSDataModel) : Response<JsonElement>
}