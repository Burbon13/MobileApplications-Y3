package com.burbon13.template.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Api {
    // private const val URL = "http://192.168.43.105:3000/" // Hotspot
    private const val URL = "http://192.168.1.8:3000/" // My home
    // private const val URL = "http://192.168.1.203:3000/" // Catrina's home

    val tokenInterceptor = TokenInterceptor()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .client(
            OkHttpClient.Builder().apply {
                this.addInterceptor(tokenInterceptor)
            }.build()
        )
        .build()
}
