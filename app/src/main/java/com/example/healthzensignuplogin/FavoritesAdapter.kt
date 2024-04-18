package com.example.healthzensignuplogin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class FavoritesAdapter(private val originalMyPostsList: List<Favorites>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var filteredMyPostsList: List<Favorites> = originalMyPostsList.sortedByDescending { it.date }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posttitle: TextView = itemView.findViewById(R.id.posttitle)
        val postcontent: TextView = itemView.findViewById(R.id.postcontent)
        val poster: TextView = itemView.findViewById(R.id.poster)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val postDate: TextView = itemView.findViewById(R.id.postDate)
    }

    interface OnItemClickListener {
        fun onItemClick(post: Favorites)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredMyPostsList[position]
        holder.posttitle.text = currentItem.posttitle
        holder.postcontent.text = currentItem.postcontent
        holder.poster.text = currentItem.poster
        holder.postDate.text = currentItem.date

        holder.cardView.setOnClickListener {
            listener.onItemClick(filteredMyPostsList[position])
        }
    }

    override fun getItemCount() = filteredMyPostsList.size

    fun filterPosts(query: String) {
        filteredMyPostsList = if (query.isEmpty()) {
            originalMyPostsList // If the query is empty, show all original posts
        } else {
            originalMyPostsList.filter { post ->
                post.posttitle.contains(query, ignoreCase = true) ||
                        post.postcontent.contains(query, ignoreCase = true) ||
                        post.poster.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged() // Notify adapter of dataset changes
    }
}