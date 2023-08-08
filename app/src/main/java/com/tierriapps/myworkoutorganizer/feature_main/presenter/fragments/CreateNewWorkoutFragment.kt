package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tierriapps.myworkoutorganizer.databinding.FragmentCreateNewWorkoutBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.CreateWorkoutRecyclerViewAdapter
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.TestViewModel


class CreateNewWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewWorkoutBinding
    private lateinit var  viewModel: TestViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewWorkoutBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TestViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listOfExercises.observe(this){
            val adapter = CreateWorkoutRecyclerViewAdapter(it)
            binding.recyclerView.layoutManager = CustomizedLayoutManager(requireContext())
            binding.recyclerView.adapter = adapter

            binding.imageViewButtonAddExercise.setOnClickListener {
                adapter.addNewExercise()
            }
        }



        binding.imageViewButtonAdd.setOnClickListener{
            viewModel.printItems()
        }
    }



}