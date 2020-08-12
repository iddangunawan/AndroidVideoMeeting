package com.mylektop.videomeeting.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

/**
 * Created by iddangunawan on 11/08/20
 */
interface ApiService {

    @POST("send")
    fun sendRemoteMessage(@HeaderMap headers: HashMap<String, String>, @Body remoteBody: String): Call<String>

}