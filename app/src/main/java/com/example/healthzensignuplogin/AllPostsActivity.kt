package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllPostsActivity : AppCompatActivity(), MyPostsAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var goPostButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_posts)

        recyclerView = findViewById(R.id.my_posts_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        goPostButton=findViewById(R.id.goPostButton)
        goPostButton.setOnClickListener {
            startActivity(Intent(this,PostActivity::class.java))
        }

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Fetch posts from Firestore
        if (userId != null) {
            // Query Firestore to get posts created by the current user
            db.collection("posts")

                .get()
                .addOnSuccessListener { documents ->
                    val posts = mutableListOf<MyPostDataClass>()
                    for (document in documents) {

                        val postTitle = document.getString("postTitle") ?: ""
                        val postContent = document.getString("postContent") ?: ""
                        val poster = document.getString("poster") ?: ""
                        val postId = document.id // Use Firestore document id as postId
                        val timestamp = document.getTimestamp("timestamp")
                      val timestampString = timestamp?.toDate()?.toString() ?: ""

                        posts.add(MyPostDataClass(postTitle, postContent, poster,postId, timestampString))
                    }
                    adapter = MyPostsAdapter(posts, this)
                    recyclerView.adapter = adapter
                }
                .addOnFailureListener { exception ->
                    // Handle errors
                    exception.printStackTrace()
                }
        } else {
            // User is not authenticated
            // Handle this case accordingly (e.g., redirect to login)
        }
    }
    override fun onItemClick(post: MyPostDataClass) {
        // Handle click event, navigate to detail post page
        val intent = Intent(this, CommunityPostDetailActivity::class.java)
        intent.putExtra("postId", post.postId) // Pass postId to detail post activity
        startActivity(intent)
    }
}

