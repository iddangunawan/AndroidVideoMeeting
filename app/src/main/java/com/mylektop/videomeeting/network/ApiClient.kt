package com.mylektop.videomeeting.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by iddangunawan on 11/08/20
 */
class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://fcm.googleapis.com/fcm/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
            }

            return retrofit!!
        }
    }
}