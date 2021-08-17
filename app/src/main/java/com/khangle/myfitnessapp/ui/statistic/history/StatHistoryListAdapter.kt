package com.khangle.myfitnessapp.ui.statistic.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.UserStat

class StatHistoryListAdapter: ListAdapter<UserStat, StatHistoryListAdapter.UserStatViewHolder>(
    statDiffUtil) {


    class UserStatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return UserStatViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserStatViewHolder, position: Int) {

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