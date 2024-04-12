package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AllPostsActivity : AppCompatActivity(), MyPostsAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        recyclerView = findViewById(R.id.my_posts_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firestore
        val db = FirebaseFirestore.getInstance()

        // Fetch posts from Firestore
        db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                val posts = mutableListOf<MyPostDataClass>()
                for (document in documents) {
                    val postTitle = document.getString("postTitle") ?: ""
                    val postContent = document.getString("postContent") ?: ""
                    val poster = document.getString("poster") ?: ""
                    val postId = document.id // Use Firestore document id as postId
                    posts.add(MyPostDataClass(postId, postTitle, postContent, poster))
                }
                adapter = MyPostsAdapter(posts, this)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle errors
                exception.printStackTrace()
            }
    }

    override fun onItemClick(post: MyPostDataClass) {
        // Handle click event, navigate to detail post page
        val intent = Intent(this, DetailPostActivity::class.java)
        intent.putExtra("postId", post.postId) // Pass postId to detail post activity
        startActivity(intent)
    }
}
