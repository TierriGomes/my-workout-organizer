package com.tierriapps.myworkoutorganizer.feature_main.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.ActivityMainBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments.MainFragment
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        createMenuThemeSetter()
        val drawerLayout = binding.drawerLayout
        val navView = binding.navigationView
        val navController = findNavController(R.id.fragmentContainerView)
        NavigationUI.setupWithNavController(binding.toolbar, navController, drawerLayout)
        NavigationUI.setupWithNavController(navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.mainFragment) {
                println("actual is mainFragment")
                binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            } else {
                println("actual is other: ${destination.displayName}")
                binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    fun createMenuThemeSetter(){
        val toolbar = binding.toolbar
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.menu_theme)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.setDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else if (it.itemId == R.id.setLightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate()
            true
        }
    }
}