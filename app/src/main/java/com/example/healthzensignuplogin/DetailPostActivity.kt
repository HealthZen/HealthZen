package com.example.healthzensignuplogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.healthzensignuplogin.MyPostDataClass
import com.example.healthzensignuplogin.R

class DetailPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)

        // Retrieve postId from intent extra
        val postId = intent.getStringExtra("postId")

        // Retrieve post details based on postId (from database or elsewhere)
        getPostDetails(postId) { post ->
            // Display post details in TextViews
            findViewById<TextView>(R.id.textViewPostTitle).text = post?.posttitle
            findViewById<TextView>(R.id.textViewPostContent).text = post?.postcontent
            findViewById<TextView>(R.id.textViewPoster).text = post?.poster
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
                    val post = MyPostDataClass(postId, postTitle, postContent, poster)
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
