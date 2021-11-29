package com.khangle.myfitnessapp.ui.statistic.otherstat

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
import com.khangle.myfitnessapp.model.AppBodyStat
import com.khangle.myfitnessapp.model.BodyStat
import com.khangle.myfitnessapp.model.Menu
import com.khangle.myfitnessapp.model.user.UserStat
import retrofit2.http.Body

class OtherStatAdapter(private val bodyList: List<AppBodyStat>) : ListAdapter<BodyStat, OtherStatAdapter.BodyStatViewHolder>(
    bodyStatDiffUtil
) {

    class BodyStatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val valueTv: TextView
        val statNameTv: TextView
        val dateTV: TextView
        lateinit var item: BodyStat

        init {
            valueTv = itemView.findViewById(R.id.valueTv)
            statNameTv = itemView.findViewById(R.id.statNameTV)
            dateTV = itemView.findViewById(R.id.dateTV)
        }

        fun bind(stat: BodyStat, unit: String, statName: String) {
            item = stat
            valueTv.text = "${item.value} ${unit}"
            statNameTv.text = statName
            dateTV.text = item.dateString

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyStatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_body_stat, parent, false)
        return BodyStatViewHolder(view)
    }

    override fun onBindViewHolder(holder: BodyStatViewHolder, position: Int) {
        val stat = getItem(position)
        val bodyStat =  bodyList.find { it.id == stat.statId }
        val unit = bodyStat?.unit ?: ""
        val statName = bodyStat?.name ?: ""
        holder.bind(stat, unit, statName)
    }
}

val bodyStatDiffUtil = object : DiffUtil.ItemCallback<BodyStat>() {
    override fun areItemsTheSame(oldItem: BodyStat, newItem: BodyStat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BodyStat,
        newItem: BodyStat
    ): Boolean {
        return oldItem.statId == newItem.statId && oldItem.value == newItem.value && oldItem.dateString == newItem.dateString
    }

}