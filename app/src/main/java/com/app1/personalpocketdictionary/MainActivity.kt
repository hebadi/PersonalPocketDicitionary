package com.app1.personalpocketdictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.app1.personalpocketdictionary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Instantiate the navController using the navHostFragment
        navController = navHostFragment.navController
        // if you have an action bar:
        // Make sure actions in the ActionBar get propagated to the NavController. this does the following:
        // Show a title in the app bar based off of the destination's label, and display the Up button whenever you're not on a top-level destination.
        setupActionBarWithNavController(navController)
        packageManager
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}