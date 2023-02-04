package com.messieyawo.artistshub.api

import com.messieyawo.artistshub.utlis.UtilConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        val client = OkHttpClient.Builder().connectTimeout(0, TimeUnit.SECONDS).readTimeout(0,
            TimeUnit.SECONDS
        ).writeTimeout(0, TimeUnit.SECONDS)
        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl(UtilConstants.baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
        return retrofit!!
    }
}