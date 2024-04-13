package com.example.healthzensignuplogin



import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val commentContent:TextView=itemView.findViewById(R.id.commentContent)
        val commentDate:TextView=itemView.findViewById(R.id.commentDate)
        val commentAuthor:TextView=itemView.findViewById(R.id.commentAuthor)

    }

//    interface OnItemClickListener {
//        fun onItemClick(post: MyPostDataClass)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = commentList[position]
        holder.commentContent.text = currentItem.content
        holder.commentDate.text = currentItem.date
        holder.commentAuthor.text = currentItem.author
//
//        holder.cardView.setOnClickListener {
//            listener.onItemClick(commentList[position])
//        }
    }

    override fun getItemCount() = commentList.size
}
