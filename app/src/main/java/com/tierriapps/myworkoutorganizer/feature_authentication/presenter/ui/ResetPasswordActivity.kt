package com.tierriapps.myworkoutorganizer.feature_authentication.presenter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.ActivityResetPasswordBinding
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.viewmodel.ResetPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        binding.buttonSendEmailToReset.setOnClickListener {
            val email = binding.authFieldCustomViewEmailToReset.getText()
            viewModel.orderReset(email)
        }
        viewModel.result.observe(this){
            binding.textView5.text = it
        }
    }
}