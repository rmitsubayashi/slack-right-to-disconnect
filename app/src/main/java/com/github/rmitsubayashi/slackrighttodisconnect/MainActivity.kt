package com.github.rmitsubayashi.slackrighttodisconnect

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity__main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__main)

        setupAppBar()
        hideKeyboardOnNavigation()
    }

    private fun setupAppBar() {
        setSupportActionBar(toolbar__main)
        attachNavControllerToAppBar()
        hideAppBarTitle()
    }

    private fun attachNavControllerToAppBar() {
        val navController = findNavController(R.id.nav_host__main)
        val appBarConfig = AppBarConfiguration(navController.graph)
        toolbar__main.setupWithNavController(navController, appBarConfig)
    }

    private fun hideAppBarTitle() {
        // initial screen
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // after navigating, ↑ is overridden by the navigation controller..
        val navController = findNavController(R.id.nav_host__main)
        navController.addOnDestinationChangedListener { _, _, _ ->
            toolbar__main.title = ""
        }
    }

    private fun hideKeyboardOnNavigation() {
        val navController = findNavController(R.id.nav_host__main)
        navController.addOnDestinationChangedListener { _, _, _ ->
            val inputMethodManger =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManger.isActive) {
                inputMethodManger.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host__main)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}