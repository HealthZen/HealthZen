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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class CommentAdapter(
    private val commentList: MutableList<Comment>,
    private val repliesMap: MutableMap<String, MutableList<RepliedComment>>,

) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private lateinit var  firestore: FirebaseFirestore
    private lateinit var  firebaseAuth: FirebaseAuth

    private lateinit var context: Context
private lateinit var repliedRecyclerView:RecyclerView


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val commentContent: TextView = itemView.findViewById(R.id.commentContent)
        val commentDate: TextView = itemView.findViewById(R.id.commentDate)
        val commentAuthor: TextView = itemView.findViewById(R.id.commentAuthor)

        val replyCommentImage: ImageView = itemView.findViewById(R.id.replycommentimage)
        val replyBoxLayout: LinearLayout = itemView.findViewById(R.id.replyBoxLayout)
        val replyInputField: EditText = itemView.findViewById(R.id.replyInputField)
        val submitReplyImageView: ImageView = itemView.findViewById(R.id.submitReplyImageView)
        val deleteCommentview: ImageView = itemView.findViewById(R.id.deleteCommentview)

        val repliedRecyclerView: RecyclerView = itemView.findViewById(R.id.repliedRecyclerView)




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


        //display replies

        val replies=repliesMap[currentItem.commentId]
        if (replies!=null) {
            val replyAdapter = RepliedAdapter(replies)
            holder.repliedRecyclerView.apply {
                layoutManager = LinearLayoutManager(holder.getContext())
                adapter = replyAdapter
            }
        }

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
        //handle submit reply
        holder.submitReplyImageView.setOnClickListener {
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
                            val replyId = UUID.randomUUID().toString()

                            val repliedCommentData = hashMapOf(
                                "repliedContent" to replyContent,
                                "repliedAuthor" to repliedAuthor,
                                "repliedAuthorId" to userId,
                                "postId" to  currentItem.postId,
                                "parentcommentId" to currentItem.commentId,
                                "timestamp" to timestamp,
                                "replyId" to replyId

                            )

                            if (currentItem.postId != null) {

                                val postRef = firestore.collection("posts").document(currentItem.postId)
                                val replyCollectionRef= postRef.collection("comments").document(currentItem.commentId)

                                replyCollectionRef.collection("replies").document(replyId)
                                    .set(repliedCommentData)
                                    .addOnSuccessListener { documentReference ->

                                        Toast.makeText(
                                            holder.itemView.context,
                                            "replied successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Add the new reply to the adapter and notify the change
                                        val newReply = RepliedComment(
                                            repliedAuthor = repliedAuthor.toString(),
                                            repliedContent = replyContent,
                                            repliedAuthorId = userId,
                                            repliedDate = timestamp.toString(),
                                            parentCommentId = currentItem.commentId,
                                            postId = currentItem.postId,
                                            replyId=replyId
                                        )
                                        addReply(currentItem.commentId, newReply)


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

            }
        }



        //set delete button visibility
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

    fun addReply(commentId: String, newReply: RepliedComment) {
        val replies = repliesMap[commentId]
        if (replies != null) {
            replies.add(newReply)
            // Notify the adapter about the data change
            notifyDataSetChanged() // Or notifyItemChanged as per your requirement
        }
    }


//    fun updateReplies(commentId: String, replies: List<RepliedComment>) {
//        repliesMap[commentId] = replies
//        notifyDataSetChanged()
//    }

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
            val commentRef = postRef.collection("comments").document(commentId)
            commentRef.delete()
                .addOnSuccessListener {
                    // Remove the deleted comment from the commentList
                    val position =
                        commentList.indexOfFirst { it.commentId == commentId }
                    if (position != -1) {
                        commentList.removeAt(position)
                        notifyItemRemoved(position) // Notify the adapter that the item has been removed
                        val replyRef = commentRef.collection("replies")
                        replyRef.get()
                            .addOnSuccessListener { replySnapshot ->
                                val batch = db.batch()
                                for (replyDoc in replySnapshot.documents) {
                                    batch.delete(replyDoc.reference)
                                }
                                batch.commit()
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Comment deleted successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener { /* Handle failure */ }
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
                .addOnFailureListener { /* Handle failure */ }
        }
    }
    }