package com.example.healthzensignuplogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues.TAG
import android.content.Intent


import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
class FavoritesActivity : AppCompatActivity(), FavoritesAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritesAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        recyclerView = findViewById(R.id.my_posts_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

// Fetch user's favorite posts from Firestore
        if (userId != null) {
            // Query Firestore to get the liked_posts subcollection of the current user
            db.collection("users").document(userId).collection("liked_posts")
                .get()
                .addOnSuccessListener { documents ->
                    val favoritePosts = mutableListOf<Favorites>()
                    val postIds = documents.map { it.id } // Extract post IDs from documents

                    // Fetch post details for each liked post ID
                    val fetchPostsTasks = postIds.map { postId ->
                        db.collection("posts").document(postId)
                            .get()
                            .addOnSuccessListener { postDocument ->
                                // Retrieve post details from the document
                                val postTitle = postDocument.getString("postTitle") ?: ""
                                val postContent = postDocument.getString("postContent") ?: ""
                                val poster = postDocument.getString("poster") ?: ""
                                val timestamp = postDocument.getTimestamp("timestamp")
                                val timestampString = timestamp?.toDate()?.toString() ?: ""
                                // Add post to the list
                                favoritePosts.add(Favorites(postTitle, postContent, poster, postId, timestampString))
                            }
                    }

                    // Wait for all fetchPostsTasks to complete
                    Tasks.whenAllSuccess<Void>(fetchPostsTasks)
                        .addOnSuccessListener {
                            // Update RecyclerView adapter with favorite posts
                            adapter = FavoritesAdapter(favoritePosts, this)
                            recyclerView.adapter = adapter
                        }
                        .addOnFailureListener { exception ->
                            // Handle errors
                            exception.printStackTrace()
                        }
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    exception.printStackTrace()
                }
        } else {

        }    }
    override fun onItemClick(post: Favorites) {
        // Handle click event, navigate to detail post page
        val intent = Intent(this, CommunityPostDetailActivity::class.java)
        intent.putExtra("postId", post.postId) // Pass postId to detail post activity
        startActivity(intent)
    }
}