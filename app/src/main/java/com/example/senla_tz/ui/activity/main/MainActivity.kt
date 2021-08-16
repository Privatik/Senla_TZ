package com.example.senla_tz.ui.activity.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.senla_tz.R
import com.example.senla_tz.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IMainNavController {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main, R.id.nav_reminder
            ), binding?.drawerLayout
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        val f = supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
        val navController = f.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun addNavController(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        binding?.apply {
            val f =
                supportFragmentManager.findFragmentById(R.id.container_main) as NavHostFragment
            val navController = f.navController

            setupActionBarWithNavController(navController, appBarConfiguration)
            navVies.setupWithNavController(navController)
        }
    }
}

interface IMainNavController {
    fun addNavController(toolbar: Toolbar)
}