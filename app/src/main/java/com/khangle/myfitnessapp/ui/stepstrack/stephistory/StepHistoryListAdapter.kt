package com.khangle.myfitnessapp.ui.stepstrack.stephistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.UserStep

class StepHistoryListAdapter : ListAdapter<UserStep, StepHistoryListAdapter.UserStepViewHolder>(
    stepDiffUtil
) {
    private var _goal = 1
    fun setGoal(goal: Int) {
        _goal = goal
        notifyDataSetChanged()
    }
    class UserStepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepTV: TextView
        val percentTV: TextView
        val dateTV: TextView
        lateinit var item: UserStep

        init {
            stepTV = itemView.findViewById(R.id.stepTV)
            percentTV = itemView.findViewById(R.id.percentTV)
            dateTV = itemView.findViewById(R.id.dateStepTV)
        }

        fun bind(step: UserStep, goal: Int) {
            item = step
            stepTV.text = "${item.steps} Bước"
            percentTV.text = "${(item.steps*100.0/ goal).toInt()} %" // dang set goal cung
            dateTV.text = item.dateString
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStepViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_step, parent, false)
        return UserStepViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserStepViewHolder, position: Int) {
        holder.bind(getItem(position), _goal)
    }
}

val stepDiffUtil = object : DiffUtil.ItemCallback<UserStep>() {
    override fun areItemsTheSame(oldItem: UserStep, newItem: UserStep): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UserStep,
        newItem: UserStep
    ): Boolean {
        return oldItem.steps == newItem.steps  && oldItem.dateString == newItem.dateString
    }

}