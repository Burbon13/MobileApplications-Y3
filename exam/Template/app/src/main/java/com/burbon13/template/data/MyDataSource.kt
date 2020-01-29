package com.burbon13.template.data

import com.burbon13.template.data.model.ProductPage
import com.burbon13.template.utils.Api
import retrofit2.http.*


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
        val result = retrofitService.getPage(1)
        val a = 3
    }

}
