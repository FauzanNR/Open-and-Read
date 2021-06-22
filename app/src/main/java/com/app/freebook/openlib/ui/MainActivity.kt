package com.app.freebook.openlib.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.app.freebook.openlib.R
import com.app.freebook.openlib.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        bottomNavController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, bottomNavController)
        val appBarConfig = AppBarConfiguration((setOf(R.id.id_home_menu, R.id.id_favorite_menu)))
        setupActionBarWithNavController(
            navController = bottomNavController,
            configuration = appBarConfig
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        bottomNavController.navigateUp()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        Glide.get(this)
            .clearMemory()
    }
}