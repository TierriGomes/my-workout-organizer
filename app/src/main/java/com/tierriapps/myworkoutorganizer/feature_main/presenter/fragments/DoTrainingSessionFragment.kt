package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.ActivityMainBinding
import com.tierriapps.myworkoutorganizer.databinding.FragmentDoTrainingSessionBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.DoTrainingSessionAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.DoTrainingSessionViewModel
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoTrainingSessionFragment : Fragment() {
    private lateinit var binding: FragmentDoTrainingSessionBinding
    private val viewModel: DoTrainingSessionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoTrainingSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("divisionName", "null")
        viewModel.getActualDivisionToDo(name?:"")
        val toolBar = (requireActivity() as MainActivity).binding.toolbar
        binding.recyclerViewDoTrainingSession.layoutManager = CustomizedLayoutManager(requireContext())
        viewModel.divisionStatus.observe(viewLifecycleOwner){ divisionForUi ->
            if (divisionForUi == null){
                val snackbar = Snackbar.make(
                    requireContext(),
                    binding.root,
                    "Cannot Load Data",
                    Snackbar.LENGTH_LONG).show()
                return@observe
            }
            toolBar.setBackgroundResource(divisionForUi.colorForButtonAndHints())
            binding.constraintLayotDoTrainingSession.setBackgroundResource(divisionForUi.colorForBackGround())
            val adapter = DoTrainingSessionAdapter(divisionForUi.exercises, divisionForUi.colorForTexts())
            binding.recyclerViewDoTrainingSession.adapter = adapter
        }
    }
}