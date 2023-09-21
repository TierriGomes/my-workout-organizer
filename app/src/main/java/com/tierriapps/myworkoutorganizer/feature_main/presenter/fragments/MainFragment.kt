package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavAction
import androidx.navigation.NavActionBuilder
import androidx.navigation.NavArgument
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.collection.LLRBNode
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.databinding.FragmentMainBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.MainWorkoutHorizontalAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.customviews.DivisionButtonView
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var toolbar: Toolbar
    private val funToEditTraining: (position: Int, char: Char) -> Unit = {position, char ->
        val snackbar = Snackbar.make(requireContext(), binding.root, "Edit training?", Snackbar.LENGTH_SHORT)
        snackbar.setAction("Yes"){
            val bundle = bundleOf()
            val thePosition = viewModel.getPositionByAllDivisions(position, char)?:return@setAction
            bundle.putInt("position", thePosition)
            findNavController().navigate(R.id.action_mainFragment_to_editTrainingFragment, bundle)
            onDestroy()
        }
        snackbar.show()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.recyclerViewMain)
        toolbar = (requireActivity() as MainActivity).binding.toolbar
        viewModel.getActualWorkoutAndSetValues()
        binding.buttonGetNextTraining.setOnClickListener {
            val division = viewModel.getNextTrainingToDo()?:return@setOnClickListener
            viewModel.selectDivision(division)
        }

        binding.buttonDoSelectedTraining.setOnClickListener {
            val string = viewModel.getActualDivisionName()?:return@setOnClickListener
            val bundle = Bundle().apply { putString("divisionName", string) }
            findNavController().navigate(R.id.action_mainFragment_to_doTrainingSessionFragment, bundle)
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.actualWorkout.observe(this){resourceWorkout ->
            when(resourceWorkout){
                is Resource.Loading -> binding.progressBarMain.visibility = View.VISIBLE
                is Resource.Error -> {
                    binding.progressBarMain.visibility = View.INVISIBLE
                    val snackBar = Snackbar.make(
                        requireContext(),
                        binding.root,
                        resourceWorkout.message?.asString(requireContext())?:"Error",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setBackgroundTint(Color.RED)
                    snackBar.setAction("Understand"){
                        snackBar.dismiss()
                    }
                    snackBar.show()
                }
                is Resource.Success -> {
                    binding.buttonGetNextTraining.visibility = View.VISIBLE
                    binding.buttonDoSelectedTraining.visibility = View.VISIBLE
                    binding.progressBarMain.visibility = View.INVISIBLE
                    binding.tvMainWorkoutName.text = resourceWorkout.content?.name?.uppercase()
                    binding.tvMainWorkoutDescription.text = resourceWorkout.content?.description
                    observeThings()
                }
            }
        }
    }

    private fun observeThings(){
        viewModel.actualTrainings.observe(this){ trainingsDone ->
            if (trainingsDone.isEmpty()){
                return@observe
            }
            val adapter = MainWorkoutHorizontalAdapter(trainingsDone, funToEditTraining)
            val layoutManager = LinearLayoutManager(requireContext())
                .apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                    reverseLayout = false
                }
            binding.recyclerViewMain.clipToPadding = false
            binding.recyclerViewMain.layoutManager = layoutManager
            adapter.scrollToLastItem(binding.recyclerViewMain)
            binding.recyclerViewMain.adapter = adapter
            binding.constraintLayoutMainFragment
                .setBackgroundResource(trainingsDone[0].colorForBackGround())
            toolbar.setBackgroundResource(trainingsDone[0].colorForButtonAndHints())
            val textColor = ContextCompat.getColor(requireContext(), trainingsDone[0].colorForTexts())
            binding.tvMainWorkoutName.setTextColor(textColor)
            binding.tvMainWorkoutDescription.setTextColor(textColor)
            binding.buttonGetNextTraining.setTextColor(textColor)
            binding.buttonDoSelectedTraining.setTextColor(textColor)
        }
        viewModel.divisionsForm.observe(this){ divisionsForm ->
            binding.linearLayoutDivisionButtonsMain.removeAllViews()
            for( d in divisionsForm){
                val bt = DivisionButtonView(requireContext())
                bt.setText(d.name.toString())
                bt.setOnClickListener {
                    viewModel.selectDivision(d)
                }
                binding.linearLayoutDivisionButtonsMain.addView(bt)
            }
        }
    }

}