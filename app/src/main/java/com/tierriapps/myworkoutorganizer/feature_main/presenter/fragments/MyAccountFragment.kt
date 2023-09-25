package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.FragmentMyAccountBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.MyAccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountFragment : Fragment() {
    private lateinit var binding: FragmentMyAccountBinding
    private val viewModel: MyAccountViewModel by viewModels()

    private var changeNameSelected = false
    private var changeEmailSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewChangeName.setOnClickListener {
            changeNameSelected = true
            changeEmailSelected = false
            binding.editTextText2NewValue.visibility = View.VISIBLE
            binding.editTextTextPasswordToNewValue.visibility = View.VISIBLE
        }

        binding.imageView3ChangeEmail.setOnClickListener {
            changeNameSelected = false
            changeEmailSelected = true
            binding.editTextText2NewValue.visibility = View.VISIBLE
            binding.editTextTextPasswordToNewValue.visibility = View.VISIBLE
        }

        binding.button10SaveUserData.setOnClickListener {
            if (binding.editTextTextPasswordToNewValue.text.isNotEmpty()){
                if (changeEmailSelected){
                    viewModel.changeUserEmail(
                        binding.editTextText2NewValue.text.toString(),
                        binding.editTextTextPasswordToNewValue.text.toString())
                } else {
                    viewModel.changeUserName(
                        binding.editTextText2NewValue.text.toString(),
                        binding.editTextTextPasswordToNewValue.text.toString()
                    )
                }
            }
        }
        binding.button11DeleteMyAccount.setOnClickListener {
            val snackbar = Snackbar.make(requireContext(), binding.root, "Are you sure?", Snackbar.LENGTH_SHORT)
            snackbar.setAction("YES"){
                viewModel.deleteMyAccount(
                    binding.editTextTextPasswordToNewValue.text.toString()
                )
            }
            snackbar.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
        viewModel.user.observe(this){
            binding.textView8Email.text = it.email
            binding.textViewUserNameProfile.text = it.name
        }
    }

}