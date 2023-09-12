package com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.RecycleritemWorkoutContainerBinding
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout

class WorkoutsContainerAdapter constructor(
    private val listOfWorkouts: List<Workout>,
    private val idSelected: Int,
    private val deleteFun: (workout: Workout) -> Unit,
    private val starFun: (workout: Workout) -> Unit
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecycleritemWorkoutContainerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return listOfWorkouts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mHolder = holder as WorkoutViewHolder
        mHolder.bind(listOfWorkouts[position])
    }

    inner class WorkoutViewHolder(view: View): ViewHolder(view){
        private val binding = RecycleritemWorkoutContainerBinding.bind(view)
        private val tvName = binding.textViewWorkoutNameRIC
        private val tvDescription = binding.textViewWorkoutDescriptionRIC
        private val btStar = binding.imageViewButtonStar
        private val btDelete = binding.imageViewButtonDeleteWorkout

        fun bind(workout: Workout){
            tvName.text = workout.name
            tvDescription.text = workout.description
            if (workout.id == idSelected){
                btStar.setImageResource(R.drawable.baseline_star_selected)
            }
            btStar.setOnClickListener {
                starFun.invoke(workout)
            }
            btDelete.setOnClickListener {
                deleteFun.invoke(workout)
            }
        }


    }
}