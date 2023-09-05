package com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tierriapps.myworkoutorganizer.databinding.RecycleritemTrainingToDoBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.ExerciseForUi
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.MyTextWatcher

class DoTrainingSessionAdapter constructor(
    private val listOfExercises: List<ExerciseForUi>,
    private val textColor: Int,
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecycleritemTrainingToDoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DoTrainingViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return listOfExercises.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mHolder = holder as DoTrainingViewHolder
        mHolder.bind(listOfExercises[position])
    }

    inner class DoTrainingViewHolder(view: View): ViewHolder(view){
        private val binding = RecycleritemTrainingToDoBinding.bind(view)
        private val tvExerciseName = binding.tvExerciseNameToDo
        private val tvExerciseDescription = binding.tvExerciseDescriptionToDo
        private val tvExerciseType = binding.textViewExerciseTypeToDo
        private val tvSeries = binding.tvSeriesToDo
        private val tvWeight = binding.editTextWeightToDo
        private val tvRest = binding.tvRestTimeToDo
        private val image = binding.imageViewExercisePictureToDo
        private val listOfReps = listOf(
            binding.editTextS1, binding.editTextS2, binding.editTextS3,
            binding.editTextS4, binding.editTextS5, binding.editTextS6
        )
        init {
            val ctextColor = ContextCompat.getColor(binding.root.context, textColor)
            tvExerciseName.setTextColor(ctextColor)
            tvExerciseDescription.setTextColor(ctextColor)
            tvSeries.setTextColor(ctextColor)
            tvWeight.setTextColor(ctextColor)
            tvRest.setTextColor(ctextColor)
            tvExerciseType.setTextColor(ctextColor)
        }

        fun bind(exercise: ExerciseForUi){
            tvExerciseName.text = exercise.name
            tvExerciseDescription.text = exercise.description
            tvSeries.text = exercise.numOfSeries.toString()
            tvWeight.setText(exercise.weight.toString())
            tvRest.text = exercise.timeOfRest.toString()
            tvExerciseType.text = exercise.type.name

            for (serie in 0 until exercise.numOfSeries!!){
                val editText = listOfReps[serie]
                editText.visibility = View.VISIBLE
                exercise.repsDone.add(mutableListOf())
                val myTextWatcher = MyTextWatcher(
                    editText,
                    if (serie < listOfReps.lastIndex) listOfReps[serie+1] else null,
                    function = {
                        val textList = mutableListOf<Int>()
                        var digitBefore: String? = null
                        for (char in editText.text){
                            if (char.isDigit()){
                                digitBefore += char
                            }else{
                                textList.add(digitBefore?.toIntOrNull()?:0)
                            }
                        }
                        exercise.repsDone[serie] = textList
                    })
                editText.addTextChangedListener(myTextWatcher)
            }
        }
    }
}