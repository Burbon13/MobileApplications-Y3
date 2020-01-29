package com.burbon13.examen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.burbon13.examen.data.model.Question
import com.burbon13.examen.data.model.Test
import com.burbon13.examen.utils.DownloadState
import com.burbon13.examen.utils.GameState
import com.burbon13.examen.utils.TAG
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONArray
import org.json.JSONObject
import java.net.URI


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var ws: WebSocketClient
    private lateinit var arrayAdapter: ArrayAdapter<Test>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
        arrayAdapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, listOf())
        testListView.adapter = arrayAdapter
        connectWebSocket()
    }

    override fun onStart() {
        super.onStart()
        retryButton.isEnabled = false
        startButton.isEnabled = false
        nextButton.isEnabled = false
        questionTextView.text = "Welcome!"
        answerEditText.isEnabled = false

        mainViewModel.downloadingState.observe(this, Observer {
            when (it) {
                DownloadState.PROGRESS -> {
                    stateTextView.text = "Downloading"
                    retryButton.isEnabled = false
                    startButton.isEnabled = false
                    nextButton.isEnabled = false
                }
                DownloadState.IDLE -> {
                    stateTextView.text = "Idle"
                    retryButton.isEnabled = false
                    startButton.isEnabled = false
                    nextButton.isEnabled = false
                }
                DownloadState.ERROR -> {
                    stateTextView.text = "Download error occurred"
                    retryButton.isEnabled = true
                    startButton.isEnabled = false
                    nextButton.isEnabled = false
                }
                DownloadState.SUCCESSFULL -> {
                    stateTextView.text = "Questions loaded!"
                    retryButton.isEnabled = false
                    startButton.isEnabled = true
                    nextButton.isEnabled = false
                }
            }
        })
        retryButton.setOnClickListener {
            mainViewModel.downloadQuestions()
        }
        startButton.setOnClickListener {
            testListView.visibility = View.INVISIBLE
            mainViewModel.startGame()
        }
        nextButton.setOnClickListener {
            //            mainViewModel.nextQuestion()
        }

        // Game observers
        mainViewModel.gameState.observe(this, Observer {
            when (it.state) {
                GameState.State.QUESTION -> {
                    questionTextView.text = it.question.text
                    stateTextView.text = "${it.secondsRemaining} seconds left"
                    startButton.isEnabled = false
                    nextButton.isEnabled = true
                    answerEditText.isEnabled = true
                }
                GameState.State.END -> {
                    startButton.isEnabled = false
                    nextButton.isEnabled = false
                    answerEditText.isEnabled = false
                    stateTextView.text = "End game :)"
                    questionTextView.text = "Your results!"
                }
                GameState.State.SCORE -> {
                    startButton.isEnabled = true
                    nextButton.isEnabled = false
                    answerEditText.isEnabled = false
                    stateTextView.text = it.score
                    questionTextView.text = "Your results!"
                }
                GameState.State.PREVIOUS -> {
                    startButton.isEnabled = true
                    nextButton.isEnabled = false
                    answerEditText.isEnabled = false
                    stateTextView.text = it.score
                    questionTextView.text = "Your results!"
                    arrayAdapter =
                        ArrayAdapter(
                            applicationContext,
                            android.R.layout.simple_list_item_1,
                            it.previousTests
                        )
                    testListView.adapter = arrayAdapter
                    testListView.visibility = View.VISIBLE
                    arrayAdapter.notifyDataSetChanged()
                }
            }
        })

        nextButton.setOnClickListener {
            mainViewModel.setAnswer(answerEditText.text.toString())
        }

        testListView.setOnItemClickListener { adapterView, view, i, l ->
            val test = adapterView.getItemAtPosition(i)

            if (test != null && test is Test) {
                Log.d(TAG, test.toString())
                if (test.sent == false) {
                    mainViewModel.retryPostQuestion(test)
                }
            }
        }
//        answerEditText.doOnTextChanged { text, start, count, after ->
//            mainViewModel.setAnswer(text.toString())
//        }
    }

    override fun onStop() {
        super.onStop()
        retryButton.setOnClickListener(null)
        startButton.setOnClickListener(null)
        nextButton.setOnClickListener(null)
    }

    private fun connectWebSocket() {
        Log.d(TAG, "Connecting web socket")

        val uri = URI("ws://192.168.43.105:3000")

        ws = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.d("Websocket", "Opened")
            }

            override fun onMessage(s: String) {
                Log.d(TAG, "WEB SOCKET MESSAGE RECEIVED")
                runOnUiThread {
                    Snackbar.make(parentLayout, "Surprise test!", 3000)
                        .setAction("TAKE TEST") {
                            val myJson = JSONArray(s)
                            runOnUiThread {
                                testListView.visibility = View.INVISIBLE
                            }
                            mainViewModel.provokeMe(
                                Question(
                                    myJson.getJSONObject(0).getInt("id"),
                                    myJson.getJSONObject(0).getString("text")
                                ),
                                Question(
                                    myJson.getJSONObject(1).getInt("id"),
                                    myJson.getJSONObject(1).getString("text")
                                    )
                            )
                        }
                        .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                        .show()
                }
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.i("Websocket", "Closed $s")
            }

            override fun onError(e: Exception) {
                Log.i("Websocket", "Error " + e.message)
            }
        }
        ws.connect()
        Log.d(TAG, "Web socket connected!")
    }
}
