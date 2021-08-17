package com.khangle.myfitnessapp.ui.statistic.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.common.BMICalculator
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.user.UserStat

class StatHistoryListAdapter: ListAdapter<UserStat, StatHistoryListAdapter.UserStatViewHolder>(
    statDiffUtil) {


    class UserStatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weightTV: TextView
        val heightTV: TextView
        val bmiTV: TextView
        lateinit var item: UserStat

        init {
            weightTV = itemView.findViewById(R.id.statWeight)
            heightTV = itemView.findViewById(R.id.statHeight)
            bmiTV = itemView.findViewById(R.id.bmiTV)
        }

        fun bind(stat: UserStat) {
            item = stat
           weightTV.text = item.weight.toString()
            heightTV.text = item.height.toString()
            bmiTV.text = BMICalculator.bmiFrom(item.weight, item.height).toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_stat, parent, false)
        return UserStatViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserStatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

val statDiffUtil = object : DiffUtil.ItemCallback<UserStat>() {
    override fun areItemsTheSame(oldItem: UserStat, newItem: UserStat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UserStat,
        newItem: UserStat
    ): Boolean {
        return oldItem.height == newItem.height && oldItem.weight == newItem.weight && oldItem.dateString == newItem.dateString
    }

}