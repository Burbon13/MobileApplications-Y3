package com.burbon13.template

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.burbon13.template.core.extensions.TAG
import com.burbon13.template.core.receivers.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private var snackbar: Snackbar? = null
    private lateinit var navController: NavController
    private lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.my_nav_host_fragment)
        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.loginFragment,
                    R.id.myObjectListFragment
                )
            )
        )
        NavigationUI.setupWithNavController(
            nav_view,
            navController
        )
        Log.d(TAG, "Registering the ConnectivityReceiver")
        connectivityReceiver = ConnectivityReceiver()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "onSupportNavigateUp()")
        return navController.navigateUp()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.d(TAG, "onNetworkConnectionChanged() -> $isConnected")
        updateConnectionSnackBar(isConnected)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(TAG, "onCreateOptionsMenu()")
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.logout_button) {
//            Log.d(TAG, "Logout")
//            logout()
//        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateConnectionSnackBar(isConnected: Boolean) {
        Log.d(TAG, "updateConnectionSnackBar")
        if (!isConnected) {
            val messageToUser = "You are offline now."
            snackbar = Snackbar.make(
                main_activity_coordinator_layout,
                messageToUser,
                Snackbar.LENGTH_LONG
            )
            snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar?.show()
        } else {
            snackbar?.dismiss()
        }
    }

//    private fun logout() {
//        Log.d(TAG, "logout()")
//        LoginRepository.logout()
//        mainViewModel.logout()
//        finish()
//    }
}
