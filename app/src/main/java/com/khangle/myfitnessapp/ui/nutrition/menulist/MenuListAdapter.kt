package com.khangle.myfitnessapp.ui.nutrition.menulist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.khangle.myfitnessapp.R
import com.khangle.myfitnessapp.model.Menu

class MenuListAdapter(val onItemclick: (item: Menu) -> Unit) :
    ListAdapter<Menu, MenuListAdapter.MenuVH>(menuDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuVH(view, onItemclick)
    }

    override fun onBindViewHolder(holder: MenuVH, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuVH(itemView: View, val onItemclick: (item: Menu) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val nameTv: TextView
        lateinit var item: Menu

        init {
            nameTv = itemView.findViewById(R.id.menuNameTv)
            itemView.findViewById<CardView>(R.id.cardItem).setOnClickListener {
                onItemclick(item)
            }
        }

        fun bind(menu: Menu) {
            item = menu
            nameTv.text = menu.name
        }
    }
}

val menuDiffUtil = object : DiffUtil.ItemCallback<Menu>() {
    override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Menu,
        newItem: Menu
    ): Boolean {
        return oldItem.breakfast == newItem.breakfast &&
                oldItem.lunch == newItem.lunch &&
                oldItem.name == newItem.name &&
                oldItem.dinner == newItem.dinner &&
                oldItem.other == newItem.other &&
                oldItem.snack == newItem.snack
    }

}