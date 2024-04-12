package com.example.healthzensignuplogin
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditMyPostActivity : AppCompatActivity() {

    private lateinit var editPostTitle: EditText
    private lateinit var editPostContent: EditText
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var postId: String // Added postId variable to store the postId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_post)

        editPostTitle = findViewById(R.id.editPostTitle)
        editPostContent = findViewById(R.id.editPostContent)
        submitButton = findViewById(R.id.submitButton)
        cancelButton = findViewById(R.id.cancelButton)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get postId from intent extra
        postId = intent.getStringExtra("postId") ?: ""

        // Fetch post details and populate EditText fields
        fetchPostDetails(postId)

        submitButton.setOnClickListener {
            // Get updated post title and content
            val updatedPostTitle = editPostTitle.text.toString()
            val updatedPostContent = editPostContent.text.toString()

            // Ensure title and content are not empty
            if (updatedPostTitle.isNotEmpty() && updatedPostContent.isNotEmpty()) {
                // Update post in Firestore
                updatePost(updatedPostTitle, updatedPostContent)
            } else {
                Toast.makeText(this@EditMyPostActivity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            startActivity(Intent(this@EditMyPostActivity, MyPostsActivity::class.java))
            finish()
        }
    }

    private fun fetchPostDetails(postId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("posts").document(postId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val postTitle = document.getString("postTitle")
                        val postContent = document.getString("postContent")
                        editPostTitle.text = Editable.Factory.getInstance().newEditable(postTitle)
                        editPostContent.text = Editable.Factory.getInstance().newEditable(postContent)
                    } else {
                        Toast.makeText(this@EditMyPostActivity, "Post not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@EditMyPostActivity, "Failed to fetch post: ${e.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
        } else {
            Toast.makeText(this@EditMyPostActivity, "User not logged in", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updatePost(updatedPostTitle: String, updatedPostContent: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val updateMap = mapOf(
                "postTitle" to updatedPostTitle,
                "postContent" to updatedPostContent
            )
            firestore.collection("posts").document(postId)
                .update(updateMap)
                .addOnSuccessListener {
                    Toast.makeText(this@EditMyPostActivity, "Post updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@EditMyPostActivity, MyPostsActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@EditMyPostActivity, "Failed to update post: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this@EditMyPostActivity, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }}



