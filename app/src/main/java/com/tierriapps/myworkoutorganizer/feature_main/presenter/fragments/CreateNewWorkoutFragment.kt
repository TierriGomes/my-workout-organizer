package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.FragmentCreateNewWorkoutBinding


class CreateNewWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewWorkoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

}