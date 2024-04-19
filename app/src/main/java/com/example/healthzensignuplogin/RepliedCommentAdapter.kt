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

class RepliedCommentAdapter(private val commentList: MutableList<Comment>) :
    RecyclerView.Adapter<RepliedCommentAdapter.ViewHolder>() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val commentContent: TextView = itemView.findViewById(R.id.commentContent)
        val commentDate: TextView = itemView.findViewById(R.id.commentDate)
        val commentAuthor: TextView = itemView.findViewById(R.id.commentAuthor)

        val replyCommentImage: ImageView = itemView.findViewById(R.id.replycommentimage)
        val replyBoxLayout: LinearLayout = itemView.findViewById(R.id.replyBoxLayout)
        val replyInputField: EditText = itemView.findViewById(R.id.replyInputField)
        val submitReplyButton: Button = itemView.findViewById(R.id.submitReplyButton)
        val deleteCommentview: ImageView = itemView.findViewById(R.id.deleteCommentview)





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
        holder.deleteCommentview.setOnClickListener {
            showDeleteConfirmationDialog(
                holder.getContext(),
                currentItem.commentId,
                currentItem.postId
            )
        }


        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        holder.submitReplyButton.setOnClickListener {
            val replyContent = holder.replyInputField.text.toString()
            if (replyContent.isNotEmpty()) {
                // Create replied comment data
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userId = currentUser!!.uid

                    val ref = firestore.collection("users").document(userId)
                    ref.get().addOnSuccessListener {
                        if (it != null) {
                            val repliedAuthor = it.data?.get("username")?.toString()
                            val timestamp = FieldValue.serverTimestamp()

                            val repliedCommentData = hashMapOf(
                                "repliedContent" to replyContent,
                                "repliedAuthor" to repliedAuthor,
                                "repliedAuthorId" to userId,
                                "postId" to  currentItem.postId,
                                "timestamp" to timestamp,

                                )

                            if (currentItem.postId != null) {
                                val replyCollectionRef= firestore.collection("comments").document(currentItem.commentId)
                                replyCollectionRef.collection("replies")
                                    .add(repliedCommentData)
                                    .addOnSuccessListener { documentReference ->
                                        Toast.makeText(
                                            holder.itemView.context,
                                            "replied successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            holder.itemView.context,
                                            "Failed to reply: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                    }
                }

                // Clear reply input field
                holder.replyInputField.text.clear()
            } else {
                // Show error or prompt user to enter reply content
            }
        }


        val commentAuthorId = currentItem.commentAuthorId
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
                        if (userId ==commentAuthorId || userId == commentPostAuthorId) {
                            holder.deleteCommentview.visibility = View.VISIBLE

                        } else {
                            holder.deleteCommentview.visibility = View.INVISIBLE
                        }
                    }

                }


        }
        // Set onClickListener on the chat image view
        holder.replyCommentImage.setOnClickListener {
            // Toggle the visibility of the reply box
            if (holder.replyBoxLayout.visibility == View.VISIBLE) {
                holder.replyBoxLayout.visibility = View.GONE
            } else {
                holder.replyBoxLayout.visibility = View.VISIBLE
            }
        }

        // Set onClickListener for submitting reply


    }

    override fun getItemCount() = commentList.size

    // Method to show the delete confirmation dialog
    private fun showDeleteConfirmationDialog(context: Context, commentId: String,postId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm Delete")
        builder.setMessage("Are you sure you want to delete this comment?")
        builder.setPositiveButton("Yes") { _, _ ->
            // Delete the comment
            deleteComment(context, commentId,postId)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Method to delete the comment
    private fun deleteComment(context: Context, commentId: String,postId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val db = FirebaseFirestore.getInstance()
        if (userId != null&&postId != null && commentId != null) {
            val postRef = firestore.collection("posts").document(postId)
            postRef.collection("comments").document(commentId)
                .delete()
                .addOnSuccessListener {
                    // Remove the deleted comment from the commentList
                    val position =
                        commentList.indexOfFirst { it.commentId == commentId }
                    if (position != -1) {
                        commentList.removeAt(position)
                        notifyItemRemoved(position) // Notify the adapter that the item has been removed
                        Toast.makeText(
                            context,
                            "Comment deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to find comment in list",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        context,
                        "Failed to delete comment: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(context, "no authorization", Toast.LENGTH_SHORT).show()
        }



    }}