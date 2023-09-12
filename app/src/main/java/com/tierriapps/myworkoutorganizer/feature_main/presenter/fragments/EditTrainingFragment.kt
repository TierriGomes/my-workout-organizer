package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.databinding.FragmentEditTrainingBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.DoTrainingSessionAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.EditTrainingRVAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.EditTrainingViewModel
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTrainingFragment : Fragment() {
    private lateinit var binding: FragmentEditTrainingBinding
    private val viewModel: EditTrainingViewModel by viewModels()
    private lateinit var toolbar: Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt("position", -1)
        if (position != -1 && position != null){
            viewModel.getActualDivisionToDo(position)
        }
        toolbar = (requireActivity() as MainActivity).binding.toolbar

        binding.buttonSaveEditTraining.setOnClickListener {
            viewModel.saveTraining()
        }

        binding.buttonDeleteEditTraining.setOnClickListener {
            val snackbar = Snackbar.make(requireContext(), binding.root, "Delete the training?", Snackbar.LENGTH_SHORT)
            snackbar.setAction("Yes"){
                viewModel.deleteTraining()
            }
            snackbar.show()
        }
        binding.recyclerViewEditTraining.layoutManager = CustomizedLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        viewModel.divisionStatus.observe(viewLifecycleOwner){ divisionForUi ->
            if (divisionForUi == null){
                val snackbar = Snackbar.make(
                    requireContext(),
                    binding.root,
                    "Cannot Load Data",
                    Snackbar.LENGTH_LONG).show()
                return@observe
            }
            val textColor = ContextCompat.getColor(requireContext(), divisionForUi.colorForTexts())
            binding.buttonSaveEditTraining.setTextColor(textColor)
            binding.buttonDeleteEditTraining.setTextColor(textColor)
            toolbar.setBackgroundResource(divisionForUi.colorForButtonAndHints())
            binding.constraintLayoutEditTraining.setBackgroundResource(divisionForUi.colorForBackGround())
            val adapter = EditTrainingRVAdapter(divisionForUi.exercises, divisionForUi.colorForTexts())
            binding.recyclerViewEditTraining.adapter = adapter
        }
        viewModel.jobStatus.observe(viewLifecycleOwner){
            if (it == null){
                binding.progressBarEditTraining.visibility = View.VISIBLE
            }else {
                binding.progressBarEditTraining.visibility = View.GONE
                Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_LONG).show()
                if (it.asString(requireContext()) == "Training Saved!"){
                    println("something")
                    findNavController().navigateUp()
                }
            }
        }
    }
}