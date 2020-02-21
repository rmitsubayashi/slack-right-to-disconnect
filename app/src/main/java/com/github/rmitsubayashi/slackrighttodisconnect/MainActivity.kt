package com.github.rmitsubayashi.slackrighttodisconnect

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity__main.*

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__main)

        setupAppBar()
    }

    private fun setupAppBar() {
        setSupportActionBar(toolbar__main)
        val navController = findNavController(R.id.nav_host__main)
        val appBarConfig = AppBarConfiguration(navController.graph)
        toolbar__main.setupWithNavController(navController, appBarConfig)
        hideAppBarTitle()
    }

    private fun hideAppBarTitle() {
        // initial screen
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // after navigating, ↑ is overridden by the navigation controller..
        val navController = findNavController(R.id.nav_host__main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment -> {toolbar__main.title = getString(R.string.label__menu__settings) }
                else -> {toolbar__main.title = ""}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu__app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host__main)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}