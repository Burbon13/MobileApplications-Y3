package com.burbon13.examen.data

import android.util.Log
import com.burbon13.examen.data.model.Answer
import com.burbon13.examen.utils.Api
import com.burbon13.examen.data.model.Question
import com.burbon13.examen.data.model.Score
import com.burbon13.examen.utils.ApiUtils
import com.burbon13.examen.utils.MyResult
import com.burbon13.examen.utils.TAG
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


object MyDataSource {

    interface RetrofitService {
//        @Headers("Content-Type: application/json")
//        @GET("/product")
//        suspend fun getPage(@Query("page") page: Int): ProductPage

        @Headers("Content-Type: application/json")
        @GET("/question")
        suspend fun getQuestions(): List<Question>

        @Headers("Content-Type: application/json")
        @POST("/quiz")
        suspend fun postQuiz(@Body answers: List<Answer>): Score
    }

    private val retrofitService: RetrofitService = Api.retrofit.create(
        RetrofitService::class.java
    )

//    suspend fun test() {
//        Log.w(TAG, "INAINTE DE TESTARE")
//        val result = retrofitService.getQuestions(1)
//        Log.w(TAG, "DUPA TESTEARE: $result")
//    }

    suspend fun getQuestions(): MyResult<List<Question>> {
        Log.d(TAG, "Fetching questions")
        return ApiUtils.retrieveResult(
            {
                retrofitService.getQuestions()
            },
            "Error occurred while fetching questions",
            "Error occurred while fetching questions"
        )
    }

    suspend fun postAnswers(answers: List<Answer>): MyResult<Score> {
        Log.d(TAG, "Posting answers: $answers")
        return ApiUtils.retrieveResult(
            {
                retrofitService.postQuiz(answers)
            },
            "Error occurred while posting questions",
            "Error occurred while posting questions"
        )
    }
}
