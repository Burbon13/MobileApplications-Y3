package com.burbon13.app.data

import android.util.Log
import com.burbon13.app.data.model.Message
import com.burbon13.app.data.model.ProductPage
import com.burbon13.app.data.model.Product
import com.burbon13.app.utils.Api
import com.burbon13.app.utils.ApiUtils
import com.burbon13.app.utils.MyResult
import com.burbon13.app.utils.TAG
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


object MessageDataSource {

    interface MessageService {
        @Headers("Content-Type: application/json")
        @GET("/message")
        suspend fun getMessages(): List<Message>

        @Headers("Content-Type: application/json")
        @PUT("/message/{id}")
        suspend fun putMessage(@Path("id") id: Long): Response<Unit>

        @Headers("Content-Type: application/json")
        @GET("/product")
        suspend fun getPage(@Query("page") page: Int): ProductPage
    }

    private val messageService: MessageService = Api.retrofit.create(
        MessageService::class.java
    )

    suspend fun getMessages(): MyResult<List<Message>> {
        Log.e(TAG, "INAINTEEEEEE")
        val hurray = messageService.getPage(1)
        Log.e(TAG, "DUPAAAA $hurray")
        Log.d(TAG, "Getting message request")
        return ApiUtils.retrieveResult(
            {
                val x = messageService.getMessages()
                Log.d(TAG, "Fetched ${x.size} messages")
                x
            },
            "Error while retrieving messages",
            "Error while retrieving messages"
        )
    }

    suspend fun update(message: Message): MyResult<Response<Unit>> {
        Log.d(TAG, "Updating message request")
        return ApiUtils.retrieveResult(
            {
                messageService.putMessage(message.id)
            },
            "Error while updating message id=${message.id}",
            "Error while updating message id=${message.id}"
        )
    }
}
