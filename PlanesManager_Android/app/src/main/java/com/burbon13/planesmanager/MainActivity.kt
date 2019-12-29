package com.burbon13.planesmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.burbon13.planesmanager.core.utils.extensions.TAG
import kotlinx.android.synthetic.main.activity_navigation.*


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

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
        supportFragmentManager.addOnBackStackChangedListener {
            Log.d(TAG, "addOnBackStackChangedListener() called ...")
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
