package com.tierriapps.myworkoutorganizer.feature_main.presenter

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.ActivityMainBinding
import com.tierriapps.myworkoutorganizer.feature_main.domain.notification_service.MyBackGroundService
import com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments.DoTrainingSessionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        if (intent?.action == MyBackGroundService.Actions.NAVIGATE_TO_THE_FRAGMENT.toString()){
            intent = null
            val navController = findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.doTrainingSessionFragment)
        }
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

    fun startDoTrainingService(gson: String){
        Intent(applicationContext, MyBackGroundService::class.java)
            .also {
                it.action = MyBackGroundService.Actions.START.toString()
                it.putExtra("division", gson)
                startService(it)
            }
    }
}