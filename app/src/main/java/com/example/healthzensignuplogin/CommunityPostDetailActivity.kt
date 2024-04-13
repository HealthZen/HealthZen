package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class CommunityPostDetailActivity : AppCompatActivity() {
    private lateinit var buttonPostComment: Button
  private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var commentEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_post_detail)
        buttonPostComment = findViewById(R.id.buttonPostComment)
        commentEditText = findViewById(R.id.commentEditText)
        recyclerView = findViewById(R.id.commentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Retrieve postId from intent extra
        val postId = intent.getStringExtra("postId")

        // Retrieve post details based on postId (from database or elsewhere)
        getPostDetails(postId) { posts ->
            // Display post details in TextViews
//            findViewById<TextView>(R.id.textViewPoster).text = post?.poster  // Set poster first
            findViewById<TextView>(R.id.textViewPostTitle).text = posts?.posttitle
            findViewById<TextView>(R.id.textViewPostContent).text = posts?.postcontent
            findViewById<TextView>(R.id.textViewPoster).text = posts?.poster
        }

        //display comment based on postid



        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Query Firestore to get posts created by the current user
            firestore.collection("comments")
                .whereEqualTo("postId", postId)
                .get()
                .addOnSuccessListener { documents ->
                    val comments = mutableListOf<Comment>()
                    for (document in documents) {
                        val commentAuthor = document.getString("commentAuthor") ?: ""
                        val commentContent = document.getString("commentContent") ?: ""
                        val timestamp = document.getTimestamp("timestamp")
                        val timestampString = timestamp?.toDate()?.toString() ?: ""

                        comments.add(Comment(commentAuthor, commentContent, timestampString))
                    }
                    commentAdapter = CommentAdapter(comments)
                    recyclerView.adapter = commentAdapter
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    exception.printStackTrace()
                }
        } else {
            // User is not authenticated
            // Handle this case accordingly (e.g., redirect to login)
        }


        buttonPostComment.setOnClickListener {

            var newCommentContent = commentEditText.text.toString()
            if (newCommentContent.isNotEmpty()) {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userId = currentUser!!.uid

                    val ref = firestore.collection("users").document(userId)
                    ref.get().addOnSuccessListener {
                        if (it != null) {
                            val newCommentAuthor = it.data?.get("username")?.toString()
                            val timestamp = FieldValue.serverTimestamp()

                            val commentData = hashMapOf(
                                "commentContent" to newCommentContent,
                                "commentAuthor" to newCommentAuthor,
                                "commentAuthorId" to userId,
                                "postId" to postId,
                                "timestamp" to timestamp

                            )
                            firestore.collection("comments")
                                .add(commentData)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@CommunityPostDetailActivity,
                                        "Comment posted successfuuly",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this,CommunityPostDetailActivity::class.java))

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@CommunityPostDetailActivity,
                                        "Failed to post comment:${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }

                }
            }

        }

    }



    // Function to retrieve post details from database or elsewhere
    private fun getPostDetails(postId: String?, callback: (MyPostDataClass?) -> Unit) {
        if (postId == null) {
            callback(null)
            return
        }


        val db = FirebaseFirestore.getInstance()

        db.collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val postTitle = document.getString("postTitle") ?: ""
                    val postContent = document.getString("postContent") ?: ""
                    val poster = document.getString("poster") ?: ""
                    val post = MyPostDataClass( postTitle, postContent, poster,postId)
                    callback(post)
                } else {
                    // Post not found or document doesn't exist
                    // Handle error or return null/default post
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                exception.printStackTrace()
                callback(null)
            }
    }}