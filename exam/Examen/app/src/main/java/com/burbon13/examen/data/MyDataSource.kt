package com.burbon13.examen.data

import android.util.Log
import com.burbon13.examen.utils.Api
import com.burbon13.examen.data.model.ProductPage
import com.burbon13.examen.utils.TAG
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

object MyDataSource {

    interface RetrofitService {
        @Headers("Content-Type: application/json")
        @GET("/product")
        suspend fun getPage(@Query("page") page: Int): ProductPage
    }

    private val retrofitService: RetrofitService = Api.retrofit.create(
        RetrofitService::class.java
    )

    suspend fun x() {
        Log.w(TAG, "INAINTE DE TESTARE")
        val result = retrofitService.getPage(1)
        Log.w(TAG, "DUPA TESTEARE: $result")
    }

}
