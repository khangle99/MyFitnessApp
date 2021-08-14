package com.khangle.myfitnessapp.ui.excercise.excdetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.khangle.myfitnessapp.R


class ImageRecyclerviewAdapter : RecyclerView.Adapter<ImageRecyclerviewAdapter.ImageViewHolder>() {
    var urlList = listOf<String>()

    fun applyUrlList(urlList: List<String>) {
        this.urlList = urlList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val get = urlList.get(position)
        holder.loadUrl(urlList.get(position))
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView
        init {
            imageView = itemView.findViewById(R.id.image_item)
        }
        fun loadUrl(urlString: String) {
            imageView.load(urlString) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
            }
        }
    }
}