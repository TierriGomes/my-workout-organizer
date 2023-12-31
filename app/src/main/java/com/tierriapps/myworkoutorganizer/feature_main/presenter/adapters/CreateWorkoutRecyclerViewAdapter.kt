package com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.RecycleritemExerciseContainerBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.ExerciseForUi
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.MyTextWatcher
import javax.inject.Inject

class CreateWorkoutRecyclerViewAdapter @Inject constructor(
    private val listOfExercises: MutableList<ExerciseForUi>,
    private val textColor: Int,
    private val hintColor: Int,
    private var itemSelectionFunction: ((actualExercise: ExerciseForUi)-> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecycleritemExerciseContainerBinding.inflate(inflater, parent, false)
        return CreateWorkoutViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return listOfExercises.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewHolder = holder as CreateWorkoutViewHolder
        viewHolder.bind(listOfExercises[position])
    }

    inner class CreateWorkoutViewHolder(val view: View): ViewHolder(view){
        private val binding = RecycleritemExerciseContainerBinding.bind(view)
        private val ctlExerciseContainer = binding.contraintLayoutExerciseContainer
        private val etExerciseName = binding.editTextExerciseName
        private val etExerciseDescription = binding.editTextExerciseDescription
        private val spExerciseType = binding.spinnerExerciseType
        private val etSeries = binding.editTextSeries
        private val etWeight = binding.editTextWeight
        private val etRest = binding.editTextRestTime
        private val image = binding.imageViewExercisePicture
        private val alert = binding.imageViewAlertExercise

        init {
            val ctextColor = ContextCompat.getColor(binding.root.context, textColor)
            val chintColor = ContextCompat.getColor(binding.root.context, hintColor)
            etExerciseName.setTextColor(ctextColor)
            etExerciseName.setHintTextColor(chintColor)
            etExerciseDescription.setTextColor(ctextColor)
            etExerciseDescription.setHintTextColor(chintColor)
            etSeries.setTextColor(ctextColor)
            etSeries.setHintTextColor(chintColor)
            etWeight.setTextColor(ctextColor)
            etWeight.setHintTextColor(chintColor)
            etRest.setTextColor(ctextColor)
            etRest.setHintTextColor(chintColor)
            val lista = ExerciseType.values().map { it.name.lowercase() }
            val adapter = CustomSpinnerAdapter(binding.root.context, android.R.layout.simple_spinner_dropdown_item, lista, ctextColor)
            spExerciseType.adapter = adapter
        }

        fun bind(exercise: ExerciseForUi) {
            etExerciseName.setText(exercise.name?:"")
            etExerciseDescription.setText(exercise.description?:"")
            spExerciseType.setSelection(exercise.type.getPosition())
            etSeries.setText(if(exercise.numOfSeries == null) "" else exercise.numOfSeries.toString())
            etWeight.setText(if(exercise.weight == null) "" else exercise.weight.toString())
            etRest.setText(if(exercise.timeOfRest == null) "" else exercise.timeOfRest.toString())
            setLogic(exercise)
        }


        private fun setLogic(exercise: ExerciseForUi){
            val actualExercise = exercise
            ctlExerciseContainer.setOnLongClickListener {
                generateSnackBarToDelete(actualExercise)
                true
            }
            image.setOnLongClickListener {
                generateSnackBarToDelete(actualExercise)
                true
            }
            etExerciseName.addTextChangedListener(MyTextWatcher(etExerciseName, etExerciseDescription) {
                actualExercise.name = etExerciseName.text.toString()
            })
            etExerciseDescription.addTextChangedListener(MyTextWatcher(etExerciseDescription, etSeries) {
                actualExercise.description = etExerciseDescription.text.toString()
            })
            etSeries.addTextChangedListener(MyTextWatcher(etSeries, etWeight) {
                actualExercise.numOfSeries = etSeries.text.toString().toIntOrNull()?:0
            })
            etWeight.addTextChangedListener(MyTextWatcher(etWeight, etRest) {
                actualExercise.weight = etWeight.text.toString().toIntOrNull()?:0
            })
            etRest.addTextChangedListener(MyTextWatcher(etRest, null){
                actualExercise.timeOfRest = etRest.text.toString().toIntOrNull()?:0
            })
            spExerciseType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    actualExercise.type = ExerciseType.values()[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Chamado quando nenhum item é selecionado
                }
            }
            alert.visibility = if (exercise.status)
                View.INVISIBLE else View.VISIBLE
            alert.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    exercise.message?.asString(binding.root.context),
                    Toast.LENGTH_SHORT).show()
            }
        }

        private fun generateSnackBarToDelete(actualExercise: ExerciseForUi){
            ctlExerciseContainer.setBackgroundResource(R.color.delete_color)
            val snackBar = Snackbar
                .make(view.context, view,"Want to delete?", Snackbar.LENGTH_SHORT )
            snackBar.setAction("yes"){
                val pos = listOfExercises.indexOf(actualExercise)
                itemSelectionFunction?.invoke(actualExercise)
                itemSelectionFunction = null
                notifyItemRemoved(pos)
                snackBar.dismiss()
            }
            snackBar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    ctlExerciseContainer.setBackgroundResource(R.drawable.shape_exercises_container)
                }
            })
            snackBar.show()
        }
        inner class CustomSpinnerAdapter(context: Context, resource: Int, items: List<String>, textColor: Int) : ArrayAdapter<String>(context, resource, items) {
            private val color = textColor
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(color) // Defina a cor desejada aqui
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(color) // Defina a cor desejada para os itens do dropdown aqui
                return view
            }
        }
    }

}