package com.tierriapps.myworkoutorganizer.feature_authentication.presenter.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.databinding.ActivityRegisterBinding
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.AuthEvents
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.viewmodel.RegisterViewModel
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
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
        viewModel.loadingState.observe(this){ isLoading ->
            binding.progressBarRegister.visibility = if(isLoading) View.VISIBLE else View.INVISIBLE
        }

        binding.buttonDoRegister.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.buttonDoRegister.windowToken, 0)

            val email = binding.authcvEmailRegister.getText()
            val pass1 = binding.authcvPassword1.getText()
            val pass2 = binding.authcvPassword2.getText()
            val name = binding.authFieldCustomViewName.getText()
            viewModel.registerUserWithEmailAndPassword(email, pass1, pass2, name)
        }
        binding.buttonBackToLogin.setOnClickListener { navigateToLogin() }
    }


    override fun onBackPressed() {
        navigateToLogin()
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

    private fun navigateToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}