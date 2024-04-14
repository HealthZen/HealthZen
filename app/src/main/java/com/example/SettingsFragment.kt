package com.example

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthzensignuplogin.CommunityPostDetailActivity
import com.example.healthzensignuplogin.MyPostDataClass
import com.example.healthzensignuplogin.MyPostsAdapter
import com.example.healthzensignuplogin.PostActivity
import com.example.healthzensignuplogin.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class SettingsFragment : Fragment(), MyPostsAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var goPostButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_all_posts, container, false)

        recyclerView = view.findViewById(R.id.my_posts_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        goPostButton = view.findViewById(R.id.goPostButton)
        goPostButton.setOnClickListener {
            startActivity(Intent(activity, PostActivity::class.java))
        }

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // Fetch posts from Firestore
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            // Query Firestore to get posts created by the current user
            firestore.collection("posts")
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
                    adapter = MyPostsAdapter(posts, this@SettingsFragment)
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

        return view
    }

    override fun onItemClick(post: MyPostDataClass) {
        // Handle click event, navigate to detail post page
        val intent = Intent(activity, CommunityPostDetailActivity::class.java)
        intent.putExtra("postId", post.postId) // Pass postId to detail post activity
        startActivity(intent)
    }
}
