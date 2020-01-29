package com.burbon13.examen.data

import android.util.Log
import com.burbon13.examen.data.local.TestDatabase
import com.burbon13.examen.data.model.Answer
import com.burbon13.examen.data.model.Question
import com.burbon13.examen.data.model.Score
import com.burbon13.examen.data.model.Test
import com.burbon13.examen.utils.MyApp
import com.burbon13.examen.utils.MyResult
import com.burbon13.examen.utils.TAG


object MyRepository {
    private val testDao = TestDatabase.getDatabase(
        MyApp.context ?: throw Exception("Unable to retrieve Application context")
    ).testDao()

    suspend fun getQuestions(): MyResult<List<Question>> {
        Log.d(TAG, "Get questions Repository")
        return MyDataSource.getQuestions()
    }

    suspend fun postAnswers(answer1: Answer, answer2: Answer): MyResult<Score> {
        Log.d(TAG, "Posting questions")
        val answers = ArrayList<Answer>()
        answers.add(answer1)
        answers.add(answer2)
        return MyDataSource.postAnswers(answers)
    }

    suspend fun getTests(): List<Test> {
        Log.d(TAG, "Retrieving all tests")
        return testDao.getAll()
    }

    suspend fun saveTest(test: Test) {
        Log.d(TAG, "saving $test")
        testDao.insert(test)
    }

    suspend fun updateTest(test: Test){
        Log.d(TAG, "Updating test $test")
        testDao.update(test)
    }
}
