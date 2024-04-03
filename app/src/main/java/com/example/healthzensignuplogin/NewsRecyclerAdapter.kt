package com.example.healthzensignuplogin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.model.ArticleDto
import com.squareup.picasso.Picasso

class NewsRecyclerAdapter(private val articleList: MutableList<ArticleDto>) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_recycler_row, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articleList[position]
        holder.titleTextView.text = article.title
        holder.sourceTextView.text = article.source.name
        Picasso.get().load(article.urlToImage)
            .error(R.drawable.no_image_icon)
            .placeholder(R.drawable.no_image_icon)
            .into(holder.imageView)
    }

    fun updateData(data: List<ArticleDto>) {
        articleList.clear()
        articleList.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.article_title)
        val sourceTextView: TextView = itemView.findViewById(R.id.article_source)
        val imageView: ImageView = itemView.findViewById(R.id.article_image_view)
    }
}
