package com.example.healthzensignuplogin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private lateinit var favoriteBtn:Button
    var isLiked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_post_detail)
        buttonPostComment = findViewById(R.id.buttonPostComment)
        commentEditText = findViewById(R.id.commentEditText)
        favoriteBtn=findViewById(R.id.favoriteBtn)
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
            findViewById<TextView>(R.id.postDate).text = posts?.date
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
                        val commentId=document.id
                        val commentAuthor = document.getString("commentAuthor") ?: ""
                        val commentContent = document.getString("commentContent") ?: ""
                        val commentAuthorId = document.getString("commentAuthorId") ?: ""
                        val postId = document.getString("postId") ?: ""
                        val timestamp = document.getTimestamp("timestamp")
                        val timestampString = timestamp?.toDate()?.toString() ?: ""

                        comments.add(Comment( commentAuthorId,commentContent, timestampString,commentId,postId,commentAuthor))
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



        val userid = userId ?: ""
        val postid = postId ?: ""
      // Initialize isLiked variable as false

        favoriteBtn.setOnClickListener {
            // Toggle the liked status when the button is clicked
            isLiked = !isLiked // Toggle the value of isLiked

            if (isLiked) {
                // If the post is not yet liked, add the like
                addLikedPost(userid, postid)
            } else {
                // If the post is already liked, remove the like
                removeLikedPost(userid, postid)
            }
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
                                    startActivity(Intent(this, CommunityPostDetailActivity::class.java).apply {
                                        putExtra("postId", postId)
                                    })

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

    // Function to check if the liked_posts subcollection exists for a user

    fun addLikedPost(userId: String, postId: String) {
        val userDocRef = firestore.collection("users").document(userId)
        val likedPostsCollectionRef = userDocRef.collection("liked_posts")

        // Create a new document in the "liked_posts" subcollection with the post ID as the document ID
        val likedPostData = hashMapOf(
            "timestamp" to System.currentTimeMillis(),
           "postId" to postId
        )

        likedPostsCollectionRef.document(postId)
            .set(likedPostData)
            .addOnSuccessListener {
                favoriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.red_heart_icon, 0, 0) // Set favorite button to red icon
                isLiked = true
                Toast.makeText(
                    this@CommunityPostDetailActivity,
                    "Liked post added successfully for user",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->

                Toast.makeText(
                    this@CommunityPostDetailActivity,
                    "Error adding liked post for user $userId: $exception",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    fun removeLikedPost(userId: String, postId: String) {
        val userDocRef = firestore.collection("users").document(userId)
        val likedPostsCollectionRef = userDocRef.collection("liked_posts")


        likedPostsCollectionRef.document(postId)
            .delete()
            .addOnSuccessListener {
                favoriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.black_heart_icon, 0, 0) // Set favorite button to red icon
                isLiked = false
                Toast.makeText(
                    this@CommunityPostDetailActivity,
                    "Liked post removed successfully for user",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { exception ->

                Toast.makeText(
                    this@CommunityPostDetailActivity,
                    "Error adding liked post for user $userId: $exception",
                    Toast.LENGTH_SHORT
                ).show()
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
                    val timestamp = document.getTimestamp("timestamp")
                    val timestampString = timestamp?.toDate()?.toString() ?: ""

                    val post = MyPostDataClass( postTitle, postContent, poster,postId,timestampString)
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
    }
}