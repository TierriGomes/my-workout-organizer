package com.tierriapps.myworkoutorganizer.feature_main.presenter

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.ActivityMainBinding
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.ui.LoginActivity
import com.tierriapps.myworkoutorganizer.feature_main.domain.notification_service.MyBackGroundService
import com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments.DoTrainingSessionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

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
        val navController = findNavController(R.id.fragmentContainerView)
        if (intent?.action == MyBackGroundService.Actions.NAVIGATE_TO_THE_FRAGMENT.toString()){
            intent = null
            navController.navigate(R.id.activity_to_doTrainingSessionFragment)
        }
        binding.menuLogoutNavigation.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.itemDoLogout){
                val snackBar = Snackbar.make(this, binding.root, "Are you sure?", Snackbar.LENGTH_SHORT)
                snackBar.setAction("Yes"){
                    FirebaseAuth.getInstance().signOut()
                    finish()
                    startActivity(
                        Intent(
                            this, LoginActivity::class.java
                        )
                    )
                }
                snackBar.show()
                return@setOnItemSelectedListener true
            }
            false
        }
        val btUserProfile = binding.navigationView.getHeaderView(0)
            .findViewById<CardView>(R.id.cardViewUserProfile)
        btUserProfile.setOnClickListener {
            print("funfou")
            navController.navigate(R.id.myAccountFragment)
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
                binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
            } else {
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