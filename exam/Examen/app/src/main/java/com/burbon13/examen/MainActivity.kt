package com.burbon13.examen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.burbon13.examen.utils.TAG
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var ws: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
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
                    // To do what you NEEDDEED
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
