package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.databinding.FragmentCreateNewWorkoutBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.CreateWorkoutRecyclerViewAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.customviews.DivisionButtonView
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.CreateNewWorkoutViewModel
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.MyTextWatcher
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CreateNewWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewWorkoutBinding
    private lateinit var toolbar: Toolbar
    private val viewModel: CreateNewWorkoutViewModel by viewModels()
    private lateinit var adapter: CreateWorkoutRecyclerViewAdapter
    private lateinit var myActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = CustomizedLayoutManager(requireContext())
        // button to add a new Division
        binding.imageViewButtonAdd.setOnClickListener {
            viewModel.addNewDivision()
        }
        // button to add a new Exercise
        binding.buttonAddExercise.setOnClickListener {
            viewModel.addNewExerciseInActualDivision()
        }

        binding.editTextCreateWorkoutName.addTextChangedListener(MyTextWatcher(
            binding.editTextCreateWorkoutName,
            binding.editTextDivisionDescription,
            function = {
                val text = binding.editTextCreateWorkoutName.text.toString()
                if (text == text.uppercase()){
                    return@MyTextWatcher
                }else {
                    binding.editTextCreateWorkoutName.setText(text.uppercase())
                    binding.editTextCreateWorkoutName.setSelection(binding.editTextCreateWorkoutName.text.length)
                }
            }
        ))
        // save division description
        binding.editTextDivisionDescription
            .addTextChangedListener(MyTextWatcher(
                actualView = binding.editTextDivisionDescription, nextView = null,
                function = {
                    val description = binding.editTextDivisionDescription.text.toString()
                    viewModel.insertDescriptionInDivision(description)
                }
            )
        )


        // save workout button at the toolbar
        myActivity = (requireActivity() as MainActivity)
        toolbar = myActivity.binding.toolbar
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.create_workout_save_button_menu)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.saveNewWorkoutMenuButton){
                val workoutName = binding.editTextCreateWorkoutName.text.toString()
                val workoutDescription = binding.editTextCreateWorkoutDescription.text.toString()
                viewModel.createAndValidateWorkout(workoutName, workoutDescription)
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.allDoneToNavigate.observe(this) {
            if (it is Resource.Success && it.content != null){
                findNavController().navigate(R.id.mainFragment)
                onDestroy()
            }
            Toast.makeText(
                requireContext(),
                it.message?.asString(requireContext()) + " ${it.content}",
                Toast.LENGTH_LONG).show()
        }
        viewModel.workoutStatus.observe(this) { resourceWorkout ->
            when(resourceWorkout){
                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.imageViewAlertForWorkout.visibility = View.VISIBLE
                    binding.imageViewAlertForWorkout.setOnClickListener {
                        Toast.makeText(
                            requireContext(),
                            resourceWorkout.message?.asString(requireContext()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.imageViewAlertForWorkout.visibility = View.INVISIBLE
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.imageViewAlertForWorkout.visibility = View.INVISIBLE
                    val message = resourceWorkout.message?.asString(requireContext())
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.listOfDivisions.observe(this) { divisions ->
            if (divisions.isNotEmpty()) {
                binding.editTextDivisionDescription.visibility = View.VISIBLE
                binding.buttonAddExercise.visibility = View.VISIBLE
            }
            binding.linearLayoutDivisionButtons.removeAllViews()
            for (division in divisions) {
                val button = DivisionButtonView(requireContext())
                button.setText(division.name.toString())
                button.setOnClickListener {
                    viewModel.selectDivision(division)
                }
                binding.linearLayoutDivisionButtons
                    .addView(button)
            }
        }

        viewModel.actualDivision.observe(this) { actualDivision ->
            if (actualDivision == null) {
                binding.constraintLayoutCreateNewWorkout.setBackgroundResource(0)
                return@observe
            }
            binding.imageViewAlertForDivision.visibility =
                if (actualDivision.status) View.INVISIBLE else View.VISIBLE
            binding.imageViewAlertForDivision.setOnClickListener {
                Toast.makeText(requireContext(),
                    actualDivision.message?.asString(requireContext()),
                    Toast.LENGTH_SHORT).show()
            }
            binding.editTextDivisionDescription.setText(actualDivision.description)
            binding.constraintLayoutCreateNewWorkout.setBackgroundResource(actualDivision.colorForBackGround())
            toolbar.setBackgroundResource(actualDivision.colorForButtonAndHints())
            adapter = CreateWorkoutRecyclerViewAdapter(
                actualDivision.exercises,
                textColor = actualDivision.colorForTexts(),
                hintColor = actualDivision.colorForButtonAndHints(),
                itemSelectionFunction = { viewModel.removeExercise(it) }
            )
            binding.recyclerView.adapter = adapter
            val editTexts = binding.root.children.filter { it is EditText }
            for (editText in editTexts) {
                editText as EditText
                editText.setTextColor(
                    ContextCompat.getColor(requireContext(), actualDivision.colorForTexts())
                )
                editText.setHintTextColor(
                    ContextCompat.getColor(requireContext(), actualDivision.colorForButtonAndHints())
                )
            }
        }
    }
    override fun onDestroy() {
        toolbar.menu.clear()
        myActivity.createMenuThemeSetter()
        toolbar.setBackgroundResource(R.color.default_toolbar)
        super.onDestroy()
    }

}