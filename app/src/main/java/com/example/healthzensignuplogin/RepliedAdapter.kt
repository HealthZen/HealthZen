package com.example.healthzensignuplogin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class RepliedAdapter(private val replyList: MutableList<RepliedComment>) :
    RecyclerView.Adapter<RepliedAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repliedAuthor: TextView = itemView.findViewById(R.id.repliedAuthorTextView)
        val repliedContent: TextView = itemView.findViewById(R.id.repliedContentTextView)
        val repliedDate: TextView = itemView.findViewById(R.id.repliedTimestampTextView)
        val deleteReplyview: ImageView = itemView.findViewById(R.id.deleteReplyview)


        fun getContext(): Context {
            return itemView.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.replied_comment_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = replyList[position]
        holder.repliedAuthor.text = currentItem.repliedAuthor
        holder.repliedContent.text = currentItem.repliedContent
        holder.repliedDate.text = currentItem.repliedDate






        //set delete button visibility
        val repliedAuthorId = currentItem.repliedAuthorId
        val postId = currentItem.postId
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (userId != null) {
            db.collection("posts")
                .document(postId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val commentPostAuthorId = document.getString("userId") ?: ""
                        if (userId ==repliedAuthorId || userId == commentPostAuthorId) {
                            holder.deleteReplyview.visibility = View.VISIBLE

                        } else {
                            holder.deleteReplyview.visibility = View.INVISIBLE
                        }
                    }

                }


        }

        // Set the click listener for the delete button
        holder.deleteReplyview.setOnClickListener {
            showDeleteConfirmationDialog(
                holder.getContext(),
                currentItem.replyId,
                currentItem.postId,
                currentItem.parentCommentId

            )
        }

    }

    override fun getItemCount(): Int {
        return replyList.size
    }

    private fun showDeleteConfirmationDialog(context: Context, commentId: String,postId: String,parentCommentId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Delete")
        builder.setMessage("Are you sure you want to delete this comment?")
        builder.setPositiveButton("Yes") { _, _ ->
            // Delete the comment
            deleteReply(context, commentId,postId,parentCommentId)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Method to delete the comment
    private fun deleteReply(context: Context, replyId: String,postId: String,parentCommentId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (userId != null&&postId != null && replyId != null) {
            val postRef = db.collection("posts").document(postId)
            val commentRef = postRef.collection("comments").document(parentCommentId)
            val replyRef =commentRef.collection("replies").document(replyId)
            replyRef.delete()
                .addOnSuccessListener {
                    // Remove the deleted comment from the commentList
                    val position =
                        replyList.indexOfFirst { it.replyId == replyId }
                    if (position != -1) {
                        replyList.removeAt(position)
                        notifyItemRemoved(position) // Notify the adapter that the item has been removed

                                        Toast.makeText(
                                            context,
                                            "Comment deleted successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                            }
                }


                .addOnFailureListener { /* Handle failure */ }
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to find comment in list",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }




