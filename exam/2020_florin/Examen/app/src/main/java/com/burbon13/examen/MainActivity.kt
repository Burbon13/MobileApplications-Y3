package com.burbon13.examen

import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.burbon13.examen.data.model.Item
import com.burbon13.examen.data.model.Product
import com.burbon13.examen.utils.TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var ws: WebSocketClient
    private lateinit var mainViewModel: MainViewModel
    private var searchFor = ""
    private lateinit var arrayAdapter: ArrayAdapter<Product>

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
        connectWebSocket()
        arrayAdapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, listOf())
        productsListView.adapter = arrayAdapter
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.searchProducts.observe(this, Observer {
            arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, it)
            productsListView.adapter = arrayAdapter
        })
        mainViewModel.status.observe(this, Observer {
            stateTextView.text = it
            searchEditText.isEnabled = it == "Idle"
        })
        mainViewModel.uploadAvailable.observe(this, Observer {
            uploadButton.isEnabled = it
        })
        mainViewModel.downloadAvailable.observe(this, Observer {
            downloadButton.isEnabled = it
        })
        downloadButton.setOnClickListener {
            mainViewModel.downloadAgain()
        }
        uploadButton.setOnClickListener {
            mainViewModel.uploadItems()
        }
        searchEditText.doOnTextChanged { text, start, count, after ->
            if (!text.toString().isEmpty()) {
                val searchText = text.toString().trim()
                if (searchText != searchFor && searchText.isNotEmpty()) {

                    searchFor = searchText

                    launch {
                        delay(2000)  //debounce timeOut
                        if (searchText != searchFor)
                            return@launch
                        searchFor = ""
                        mainViewModel.search(searchText)
                    }
                }
            }
        }
        productsListView.setOnItemClickListener { adapterView, view, pos, id ->
            val item = arrayAdapter.getItem(pos)
            Log.d(TAG, item.toString())
            openAlertDialog(item)
        }
    }

    private fun openAlertDialog(product: Product?) {
        if (product == null) {
            return
        }

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Set quantity")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setRawInputType(Configuration.KEYBOARD_12KEY)
        alert.setView(input)
        alert.setPositiveButton("OK") { dialogInterface, i ->
            Log.d(TAG, "SHE SAID YES")
            if (input.text.toString().isNotEmpty())
                mainViewModel.modifyQuantity(product.code, input.text.toString().toInt())
        }
        alert.show()
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
                    mainViewModel.wsReceived()
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

    override fun onStop() {
        super.onStop()
        downloadButton.setOnClickListener(null)
    }
}
