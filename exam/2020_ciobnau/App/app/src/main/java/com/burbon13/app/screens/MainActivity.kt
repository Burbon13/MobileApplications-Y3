package com.burbon13.app.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.burbon13.app.R
import com.burbon13.app.data.model.Message
import com.burbon13.app.utils.TAG
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI


class MainActivity : AppCompatActivity() {
    private lateinit var ws: WebSocketClient

    private lateinit var mainViewModel: MainViewModel

    private lateinit var listAdapter: ExpandableListAdapter
    private lateinit var expListView: ExpandableListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        expListView = findViewById<View>(R.id.lvExp) as ExpandableListView
        // preparing list data
        listAdapter =
            ExpandableListAdapter(this, listOf(), mainViewModel)

        expListView.setAdapter(listAdapter)

        connectWebSocket()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.messages.observe(this, Observer {
            Log.d(TAG, "Messages loaded")
            listAdapter.messageDto = it
            listAdapter.notifyDataSetChanged()
        })
    }

    private fun connectWebSocket() {
        Log.d(TAG, "Connecting web socket")

        val uri = URI("ws://192.168.1.9:3000")

        ws = object : WebSocketClient(uri) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.d("Websocket", "Opened")
            }

            override fun onMessage(s: String) {

                val answer = JSONObject(s)
                val message = Message(answer.getLong("id"),
                    answer.getString("text"),
                    answer.getBoolean("read"),
                    answer.getString("sender"),
                    answer.getLong("created"))
                mainViewModel.newMessage(message)
            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                Log.i("Websocket", "Closed $s")
            }

            override fun onError(e: Exception) {
                Log.i("Websocket", "Error " + e.message)
            }
        }
        ws.connect()
    }
}
