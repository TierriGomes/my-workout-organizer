package com.tierriapps.myworkoutorganizer.feature_authentication.presenter.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.databinding.ActivityLoginBinding
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.AuthEvents
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.viewmodel.LoginViewModel
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadingState.observe(this){isLoading ->
            binding.progressBarLogin.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        }
        viewModel.resultEvent.observe(this){ event ->
            event.let { authEvents ->
                when(authEvents){
                    is AuthEvents.ErrorSnackBar ->
                        showErrorSnackBar(authEvents.message?.asString(this)?:"")
                    is AuthEvents.SuccessSnackBar ->
                        showSuccessSnackBar(authEvents.message?.asString(this)?:"")
                    is AuthEvents.NavigateToMainAuthenticated ->
                        navigateToMain()
                    null -> TODO()
                }
            }
        }
        binding.buttonLogin.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.buttonLogin.windowToken, 0)

            val email = binding.authcvEmailLogin.getText()
            val pass = binding.authcvPassword.getText()
            viewModel.loginWithEmailAndPassword(email, pass)
        }

        binding.authcvPassword.setOnKeyListener {view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER){
                binding.buttonLogin.performClick()
            }
        }

        binding.buttonRegisterLogin.setOnClickListener { navigateToRegister() }
    }

    private fun showErrorSnackBar(message: String){
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(Color.RED)
        snackBar.show()
    }

    private fun showSuccessSnackBar(message: String){
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(Color.GREEN)
        snackBar.show()
    }

    private fun navigateToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

}