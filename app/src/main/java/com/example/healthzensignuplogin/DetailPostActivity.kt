package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.example.healthzensignuplogin.MyPostDataClass
import com.example.healthzensignuplogin.R
import com.google.firebase.auth.FirebaseAuth

class DetailPostActivity : AppCompatActivity() {
    private lateinit var editPostButton:Button
    private lateinit var deletePostButton:Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)
        editPostButton=findViewById(R.id.editPostButton)
        deletePostButton=findViewById(R.id.deletePostButton)
        firebaseAuth=FirebaseAuth.getInstance()

        // Retrieve postId from intent extra
        val postId = intent.getStringExtra("postId")

        // Retrieve post details based on postId (from database or elsewhere)
        getPostDetails(postId) { post ->
            // Display post details in TextViews
            findViewById<TextView>(R.id.textViewPostTitle).text = post?.posttitle
            findViewById<TextView>(R.id.textViewPostContent).text = post?.postcontent
            findViewById<TextView>(R.id.textViewPoster).text = post?.poster
        }


        editPostButton.setOnClickListener {
            val intent = Intent(this, EditMyPostActivity::class.java)
            intent.putExtra("postId", postId)
            startActivity(intent)
        }

        deletePostButton.setOnClickListener {
            val postId = postId ?: return@setOnClickListener
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete this post?")
            builder.setPositiveButton("Yes") { dialog, which ->
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("posts").document(postId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this@DetailPostActivity, "Post deleted successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@DetailPostActivity, MyPostsActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@DetailPostActivity, "Failed to delete post: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@DetailPostActivity, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
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