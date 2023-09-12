package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.FragmentMyWorkoutsBinding
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.WorkoutsContainerAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.MyWorkoutsViewModel
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWorkoutsFragment : Fragment() {
    private lateinit var binding: FragmentMyWorkoutsBinding
    private val viewModel: MyWorkoutsViewModel by viewModels()

    private val deleteFun: (workout: Workout) -> Unit = {workout ->
        val snackbar = Snackbar.make(requireContext(), binding.root, "Delete this workout?", Snackbar.LENGTH_SHORT)
        snackbar.setAction("Yes"){
            viewModel.deleteWorkout(workout)
        }
        snackbar.show()
    }
    private val starFun: (workout: Workout) -> Unit = {workout ->
        viewModel.setActualWorkout(workout)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()
        binding.recyclerViewMyWorkouts.layoutManager = CustomizedLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.allWorkouts.observe(viewLifecycleOwner){ workoutList ->
            viewModel.actualWorkout.observe(viewLifecycleOwner){ actualWorkout ->
                val adapter = WorkoutsContainerAdapter(
                    workoutList,
                    actualWorkout?.id?:0,
                    deleteFun = deleteFun,
                    starFun = starFun
                    )
                binding.recyclerViewMyWorkouts.adapter = adapter
            }
        }
    }

}