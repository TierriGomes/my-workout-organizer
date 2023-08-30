package com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tierriapps.myworkoutorganizer.databinding.RecycleritemDivisionsMainContainerBinding
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForUi
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager

class MainWorkoutHorizontalAdapter constructor(
    private val trainingsDone: List<DivisionForUi>
): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecycleritemDivisionsMainContainerBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHorizontalViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return trainingsDone.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mHolder = holder as MainHorizontalViewHolder
        mHolder.bind(trainingsDone[position], position)
    }
    inner class MainHorizontalViewHolder(view: View): ViewHolder(view){
        private val binding = RecycleritemDivisionsMainContainerBinding.bind(view)
        private val tvDescription = binding.textViewDivisionsDescription
        private val tvDayAndPosition = binding.textViewDivisionsDoneAndActualDay
        private val recyclerView = binding.recyclerViewExercisesPerDay

        fun bind(divisionForUi: DivisionForUi, position: Int){
            val textColor = ContextCompat.getColor(binding.root.context, divisionForUi.colorForTexts())
            tvDescription.text = divisionForUi.description
            tvDescription.setTextColor(textColor)
            tvDayAndPosition.text = "Day: ${divisionForUi.day}  -  Position: ${position+1}"
            tvDayAndPosition.setTextColor(textColor)

            val adapter = MainWorkoutRecyclerViewAdapter(
                divisionForUi.exercises,
                divisionForUi.colorForTexts(),
                divisionForUi.colorForButtonAndHints()
            )
            recyclerView.layoutManager = CustomizedLayoutManager(binding.root.context)
            recyclerView.adapter = adapter
        }
    }
}