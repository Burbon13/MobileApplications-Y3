package com.burbon13.planesmanager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
    private lateinit var navController: NavController
    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
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
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showConnectionSnackBarMessage(isConnected)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout_button) {
            Log.d(TAG, "Logout")
            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConnectionSnackBarMessage(isConnected: Boolean) {
        if (!isConnected) {
            val messageToUser = "You are offline now."
            mSnackBar = Snackbar.make(
                coordinator_layout,
                messageToUser,
                Snackbar.LENGTH_LONG
            )
            mSnackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            mSnackBar?.show()
        } else {
            mSnackBar?.dismiss()
        }
    }

    private fun logout() {
        LoginRepository.logout()
        finish()
    }
}
