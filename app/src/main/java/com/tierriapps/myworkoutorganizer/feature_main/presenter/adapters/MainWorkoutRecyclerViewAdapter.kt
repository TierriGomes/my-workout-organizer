package com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tierriapps.myworkoutorganizer.databinding.RecycleritemExerciseMainContainerBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.ExerciseForUi

class MainWorkoutRecyclerViewAdapter constructor(
    private val exercisesList: MutableList<ExerciseForUi>,
    private val textColor: Int,
    private val hintColor: Int
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecycleritemExerciseMainContainerBinding
            .inflate(LayoutInflater.from(parent.context))
        return MainWorkoutViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return exercisesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mHolder = holder as MainWorkoutViewHolder
        mHolder.bind(exercisesList[position])
    }

    inner class MainWorkoutViewHolder(view: View): ViewHolder(view){
        private val binding = RecycleritemExerciseMainContainerBinding.bind(view)
        private val tvExerciseName = binding.tvExerciseName
        private val tvExerciseDescription = binding.tvExerciseDescription
        private val tvExerciseType = binding.textViewExerciseType
        private val tvSeries = binding.tvSeries
        private val tvWeight = binding.tvWeight
        private val tvRest = binding.tvRestTime
        private val image = binding.imageViewExercisePicture
        private val listOfReps = listOf(
            binding.textViewS1, binding.textViewS2, binding.textViewS3,
            binding.textViewS4, binding.textViewS5, binding.textViewS6
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
            tvWeight.text = exercise.weight.toString()
            tvRest.text = exercise.timeOfRest.toString()
            tvExerciseType.text = exercise.type.name
            for ((k, r) in exercise.repsDone.withIndex()){
                var text = "("
                for(n in r){
                    text += "-$n"
                }
                //text.replaceFirst('-', '(')
                text += ")"
                listOfReps[k].text = exercise.repsDone[k].toString()
                listOfReps[k].visibility = View.VISIBLE
            }
        }
    }
}