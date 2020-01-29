package com.burbon13.examen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burbon13.examen.data.MyDataSource
import com.burbon13.examen.data.MyRepository
import com.burbon13.examen.data.model.Answer
import com.burbon13.examen.data.model.Question
import com.burbon13.examen.data.model.Test
import com.burbon13.examen.utils.DownloadState
import com.burbon13.examen.utils.GameState
import com.burbon13.examen.utils.MyResult
import com.burbon13.examen.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel() {
    // Download state
    val downloadingState = MutableLiveData<DownloadState>()
    lateinit var questions: List<Question>

    // Game state
    val gameState = MutableLiveData<GameState>()
    val currentQuestion = MutableLiveData<Question>()
    val timeState = MutableLiveData<String>()

    var answer1 = "---"
    var answer2 = "---"
    var questionNr = -1
    var seconds = -1

    init {
//        Log.d(TAG, "Init MainViewModel")
//        viewModelScope.launch(Dispatchers.IO) {
//            Log.d(TAG, "Before test!")
////            MyDataSource.test()
//            Log.d(TAG, "After test!")
//        }
        Log.d(TAG, "Initializing MainViewModel")
        downloadQuestions()
    }

    fun downloadQuestions() {
        downloadingState.value = DownloadState.PROGRESS
        viewModelScope.launch(Dispatchers.IO) {
            val questionsResult = MyRepository.getQuestions()
            if (questionsResult is MyResult.Error) {
                downloadingState.postValue(DownloadState.ERROR)
            } else {
                val questionsList = (questionsResult as MyResult.Success).data
                questions = questionsList
                downloadingState.postValue(DownloadState.SUCCESSFULL)
            }
        }
    }

    fun setAnswer(answer: String) {
        if (questionNr == 1) {
            answer1 = answer
            seconds = 0
        } else if (questionNr == 2) {
            answer2 = answer
            seconds = 0
        }
    }

    fun startGame() {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting game, selecting questions")
            val question1 = questions.random()
            val question2 = questions.random()

            answer1 = "0"
            answer2 = "0"

            Log.d(TAG, "Question1")
            seconds = 10
            gameState.postValue(GameState(GameState.State.QUESTION, question1, seconds))
            questionNr = 1
            while (seconds > 0) {
                seconds -= 1
                delay(1000)
                gameState.postValue(GameState(GameState.State.QUESTION, question1, seconds))
            }

            Log.d(TAG, "Question2")
            seconds = 10
            gameState.postValue(GameState(GameState.State.QUESTION, question2, seconds))
            questionNr = 2
            while (seconds > 0) {
                seconds -= 1
                delay(1000)
                gameState.postValue(GameState(GameState.State.QUESTION, question2, seconds))
            }

            Log.d(TAG, " End game")
            gameState.postValue(GameState(GameState.State.END, question1, 0))
            questionNr = -1

            Log.d(TAG, "Answer1=$answer1 and Answer2=$answer2")

            Log.d(TAG, "Retrieving score")
            withContext(Dispatchers.IO) {
                if (answer1 == "")
                    answer1 = "0"
                if (answer2 == "")
                    answer2 = "0"
                val score = MyRepository.postAnswers(
                    Answer(question1.id, answer1.toInt()),
                    Answer(question2.id, answer2.toInt())
                )
                if (score is MyResult.Error) {
                    Log.w(TAG, "WRONG ${score.message}")
                    MyRepository.saveTest(
                        Test(
                            0,
                            question1.id,
                            question2.id,
                            false,
                            0,
                            answer1,
                            answer2
                        )
                    )
                    Log.d(TAG, "Previous tests posting")
                    val previousTests = MyRepository.getTests()
                    gameState.postValue(
                        GameState(
                            GameState.State.PREVIOUS,
                            question1,
                            0,
                            "Unable to connect",
                            previousTests
                        )
                    )
                } else {
                    val theScore = score as MyResult.Success
                    gameState.postValue(
                        GameState(
                            GameState.State.SCORE,
                            question1,
                            0,
                            "Correct answers: ${theScore.data.count}"
                        )
                    )
                    MyRepository.saveTest(
                        Test(
                            0,
                            question1.id,
                            question2.id,
                            true,
                            theScore.data.count,
                            answer1,
                            answer2
                        )
                    )

                    Log.d(TAG, "Previous tests posting")
                    val previousTests = MyRepository.getTests()
                    gameState.postValue(
                        GameState(
                            GameState.State.PREVIOUS,
                            question1,
                            0,
                            "Correct answers: ${theScore.data.count}",
                            previousTests
                        )
                    )
                }

            }
        }
    }

    fun setNext() {
        seconds = 0
    }

    fun retryPostQuestion(test: Test) {
        viewModelScope.launch(Dispatchers.IO) {
            val score = MyRepository.postAnswers(
                Answer(test.idQ1, test.answer1.toInt()),
                Answer(test.idQ2, test.answer2.toInt())
            )
            if (score is MyResult.Error) {
                Log.w(TAG, "WRONG ${score.message}")
            } else {

                MyRepository.updateTest(
                    Test(
                        test.id,
                        test.idQ1,
                        test.idQ2,
                        true,
                        (score as MyResult.Success).data.count,
                        test.answer1,
                        test.answer2
                    )
                )

                Log.d(TAG, "Previous tests posting")
                val previousTests = MyRepository.getTests()
                gameState.postValue(
                    GameState(
                        GameState.State.PREVIOUS,
                        Question(0, ""),
                        0,
                        "Retried! Score: ${test.score}",
                        previousTests
                    )
                )
            }
        }
    }

    fun provokeMe(question1: Question, question2: Question) {
        viewModelScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting game, selecting questions")

            answer1 = "0"
            answer2 = "0"

            Log.d(TAG, "Question1")
            seconds = 10
            gameState.postValue(GameState(GameState.State.QUESTION, question1, seconds))
            questionNr = 1
            while (seconds > 0) {
                seconds -= 1
                delay(1000)
                gameState.postValue(GameState(GameState.State.QUESTION, question1, seconds))
            }

            Log.d(TAG, "Question2")
            seconds = 10
            gameState.postValue(GameState(GameState.State.QUESTION, question2, seconds))
            questionNr = 2
            while (seconds > 0) {
                seconds -= 1
                delay(1000)
                gameState.postValue(GameState(GameState.State.QUESTION, question2, seconds))
            }

            Log.d(TAG, " End game")
            gameState.postValue(GameState(GameState.State.END, question1, 0))
            questionNr = -1

            Log.d(TAG, "Answer1=$answer1 and Answer2=$answer2")

            Log.d(TAG, "Retrieving score")
            withContext(Dispatchers.IO) {
                if (answer1 == "")
                    answer1 = "0"
                if (answer2 == "")
                    answer2 = "0"
                val score = MyRepository.postAnswers(
                    Answer(question1.id, answer1.toInt()),
                    Answer(question2.id, answer2.toInt())
                )
                if (score is MyResult.Error) {
                    Log.w(TAG, "WRONG ${score.message}")
                    MyRepository.saveTest(
                        Test(
                            0,
                            question1.id,
                            question2.id,
                            false,
                            0,
                            answer1,
                            answer2
                        )
                    )
                    Log.d(TAG, "Previous tests posting")
                    val previousTests = MyRepository.getTests()
                    gameState.postValue(
                        GameState(
                            GameState.State.PREVIOUS,
                            question1,
                            0,
                            "Unable to connect",
                            previousTests
                        )
                    )
                } else {
                    val theScore = score as MyResult.Success
                    gameState.postValue(
                        GameState(
                            GameState.State.SCORE,
                            question1,
                            0,
                            "Correct answers: ${theScore.data.count}"
                        )
                    )
                    MyRepository.saveTest(
                        Test(
                            0,
                            question1.id,
                            question2.id,
                            true,
                            theScore.data.count,
                            answer1,
                            answer2
                        )
                    )

                    Log.d(TAG, "Previous tests posting")
                    val previousTests = MyRepository.getTests()
                    gameState.postValue(
                        GameState(
                            GameState.State.PREVIOUS,
                            question1,
                            0,
                            "Correct answers: ${theScore.data.count}",
                            previousTests
                        )
                    )
                }

            }
        }
    }
}