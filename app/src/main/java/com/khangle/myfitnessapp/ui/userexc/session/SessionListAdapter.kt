package com.khangle.myfitnessapp.ui.userexc.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.user.Session

class SessionListAdapter constructor(private val onClick: (Session) -> Unit ): ListAdapter<Session, SessionListAdapter.SessionHolder>(sessionDiff){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return SessionHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: SessionHolder, position: Int) {
        holder.configureWith(getItem(position))
    }

    class SessionHolder(itemView: View, private val onClick: (Session) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val nameTV: TextView
        private lateinit var session: Session

        init {
            nameTV = itemView.findViewById(R.id.sessioNameTv)
            itemView.setOnClickListener {
                onClick(session)
            }
        }

        fun configureWith(session: Session) {
            this.session = session
            nameTV.text = session.name
        }
    }
}

val sessionDiff = object : DiffUtil.ItemCallback<Session>() {
    override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
        return oldItem.name == newItem.name
    }

}