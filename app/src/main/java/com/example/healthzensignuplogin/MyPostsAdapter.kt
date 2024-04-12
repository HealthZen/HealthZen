package com.example.healthzensignuplogin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyPostsAdapter(private val myPostsList: List<MyPostDataClass>) :
    RecyclerView.Adapter<MyPostsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posttitle: TextView = itemView.findViewById(R.id.posttitle)
        val postcontent: TextView = itemView.findViewById(R.id.postcontent)
        val poster: TextView = itemView.findViewById(R.id.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = myPostsList[position]
        holder.posttitle.text = currentItem.posttitle
        holder.postcontent.text = currentItem.postcontent
        holder.poster.text = currentItem.poster
    }

    override fun getItemCount() = myPostsList.size
}
