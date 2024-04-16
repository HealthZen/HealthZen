package com.example.healthzensignuplogin



import android.content.Context
import android.content.Intent
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

class CommentAdapter(private val commentList: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val commentContent: TextView = itemView.findViewById(R.id.commentContent)
        val commentDate: TextView = itemView.findViewById(R.id.commentDate)
        val commentAuthor: TextView = itemView.findViewById(R.id.commentAuthor)
        val deleteCommentButton: Button = itemView.findViewById(R.id.deleteCommentButton)

        // Function to get the context from the item view
        fun getContext(): Context {
            return itemView.context
        }
    }

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

        // Set the click listener for the delete button
        holder.deleteCommentButton.setOnClickListener {
            showDeleteConfirmationDialog(holder.getContext(), currentItem.commentId)
        }
    }

    override fun getItemCount() = commentList.size

    // Method to show the delete confirmation dialog
    private fun showDeleteConfirmationDialog(context: Context, commentId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Delete")
        builder.setMessage("Are you sure you want to delete this comment?")
        builder.setPositiveButton("Yes") { _, _ ->
            // Delete the comment
            deleteComment(context, commentId)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Method to delete the comment
    private fun deleteComment(context: Context, commentId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {


            val db = FirebaseFirestore.getInstance()
            db.collection("comments").document(commentId)
                .delete()
                .addOnSuccessListener {
                    // Remove the deleted comment from the commentList
                    val position = commentList.indexOfFirst { it.commentId == commentId }
                    if (position != -1) {
                        commentList.removeAt(position)
                        notifyItemRemoved(position) // Notify the adapter that the item has been removed
                        Toast.makeText(context, "Comment deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to find comment in list", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed to delete comment: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }


}
