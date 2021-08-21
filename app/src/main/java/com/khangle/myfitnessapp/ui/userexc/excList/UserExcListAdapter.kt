package com.khangle.myfitnessapp.ui.userexc.excList

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.Session
import com.khangle.myfitnessapp.model.user.UserExcTuple
import com.khangle.myfitnessapp.model.user.UserExcercise

class UserExcListAdapter constructor(private val onClick: (UserExcTuple) -> Unit) :
    ListAdapter<UserExcTuple, UserExcListAdapter.UserExcHolder>(userExcDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserExcHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_userexc, parent, false)
        return UserExcHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: UserExcHolder, position: Int) {
        holder.configureWith(getItem(position))
    }

    class UserExcHolder(itemView: View, private val onClick: (UserExcTuple) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val nameTV: TextView
        private val difficultyTV: TextView
        private lateinit var userExc: UserExcTuple

        init {
            nameTV = itemView.findViewById(R.id.userExcNameTV)
            difficultyTV = itemView.findViewById(R.id.userExcDiffTV)
            itemView.setOnClickListener {
                onClick(userExc)
            }
        }

        fun configureWith(userExc: UserExcTuple) {
            this.userExc = userExc
            val spannable = SpannableString("Excercise Deleted")
            spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                0, // start
                spannable.length , // end
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            nameTV.text = userExc.excercise?.name ?: spannable
            difficultyTV.text = userExc.excercise?.difficulty
        }
    }
}

val userExcDiff = object : DiffUtil.ItemCallback<UserExcTuple>() {
    override fun areItemsTheSame(oldItem: UserExcTuple, newItem: UserExcTuple): Boolean {
        return oldItem.timeInfo?.id == newItem.timeInfo?.id
    }

    override fun areContentsTheSame(oldItem: UserExcTuple, newItem: UserExcTuple): Boolean {
        return oldItem.timeInfo?.noGap == newItem.timeInfo?.noGap &&
                oldItem.timeInfo?.noTurn == newItem.timeInfo?.noTurn &&
                oldItem.timeInfo?.noSec == newItem.timeInfo?.noSec
    }

}