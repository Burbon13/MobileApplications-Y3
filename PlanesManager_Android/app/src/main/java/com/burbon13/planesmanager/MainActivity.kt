package com.burbon13.planesmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_navigation.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(R.id.my_nav_host_fragment),
            drawer_layout
        )
        NavigationUI.setupWithNavController(
            nav_view,
            findNavController(R.id.my_nav_host_fragment)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_controller_view_tag)
        return NavigationUI.navigateUp(navController, drawer_layout)
    }
}
