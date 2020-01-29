package com.burbon13.examen.data

import android.util.Log
import com.burbon13.examen.data.model.Item
import com.burbon13.examen.utils.Api
import com.burbon13.examen.data.model.ProductPage
import com.burbon13.examen.utils.ApiUtils
import com.burbon13.examen.utils.MyResult
import com.burbon13.examen.utils.TAG
import retrofit2.Response
import retrofit2.http.*


object MyDataSource {

    interface RetrofitService {
        @Headers("Content-Type: application/json")
        @GET("/product")
        suspend fun getPage(@Query("page") page: Int): ProductPage

        @Headers("Content-Type: application/json")
        @POST("/item")
        suspend fun postItem(@Body item: Item): Response<Unit>
    }

    private val retrofitService: RetrofitService = Api.retrofit.create(
        RetrofitService::class.java
    )

    suspend fun getPage(page: Int): MyResult<ProductPage> {
        Log.d(TAG, "Fetching page $page")
        return ApiUtils.retrieveResult(
            {
                retrofitService.getPage(page)
            },
            "Error occurred while loading page $page",
            "Error occurred while loading page $page"
        )
    }

    suspend fun uploadItem(item: Item): MyResult<Response<Unit>> {
        return ApiUtils.retrieveResult(
            {
                retrofitService.postItem(item)
            },
            "Error occurred while uploading $item",
            "Error occurred while uploading $item"
        )
    }

    suspend fun test() {
        Log.w(TAG, "INAINTE DE TESTARE")
        val result = retrofitService.getPage(1)
        Log.w(TAG, "DUPA TESTEARE: $result")
    }

}
