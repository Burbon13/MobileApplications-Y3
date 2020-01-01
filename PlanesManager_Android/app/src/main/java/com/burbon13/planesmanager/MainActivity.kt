package com.burbon13.planesmanager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.burbon13.planesmanager.auth.data.LoginRepository
import com.burbon13.planesmanager.core.utils.extensions.TAG
import com.burbon13.planesmanager.core.utils.receivers.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_navigation.*


class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    private var snackbar: Snackbar? = null
    private var showLogout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate()")
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_navigation)
        Log.d(TAG, "Setting up the navigation controller")
        navController = findNavController(R.id.my_nav_host_fragment)
        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.planesFragment,
                    R.id.loginFragment
                )
            )
        )
        NavigationUI.setupWithNavController(
            nav_view,
            navController
        )
        Log.d(TAG, "Registering the ConnectivityReceiver")
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.loggedIn.observe(this, Observer { loggedIn ->
            Log.e(TAG, "Login state modified: $loggedIn")
            showLogout = loggedIn
            invalidateOptionsMenu()
        })
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.loggedIn.removeObservers(this)
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
        if(showLogout) {
            menuInflater.inflate(R.menu.menu_action_bar, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_button) {
            Log.d(TAG, "Logout")
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateConnectionSnackBar(isConnected: Boolean) {
        Log.d(TAG, "updateConnectionSnackBar")
        if (!isConnected) {
            val messageToUser = "You are offline now."
            snackbar = Snackbar.make(
                coordinator_layout,
                messageToUser,
                Snackbar.LENGTH_LONG
            )
            snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar?.show()
        } else {
            snackbar?.dismiss()
        }
    }

    private fun logout() {
        Log.d(TAG, "logout()")
        LoginRepository.logout()
        mainViewModel.logout()
        finish()
    }
}
