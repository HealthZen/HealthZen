package com.example.healthzensignuplogin


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MyPostsActivity : AppCompatActivity(), MyPostsAdapter.OnItemClickListener {

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
                    posts.add(MyPostDataClass(postTitle, postContent, poster,postId))
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


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_posts)
//
//        auth = FirebaseAuth.getInstance()
//        firestore = FirebaseFirestore.getInstance()
//
//        recyclerView = findViewById(R.id.my_posts_recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        myPostsList = arrayListOf()
//        val userId = auth.currentUser?.uid
//
//            firestore.collection("post").get()
//                .addOnSuccessListener {
//                    if (!it.isEmpty){
//                        for (data in it.documents){
//                            val post:MyPostDataClass?=data.toObject(MyPostDataClass::class.java)
//                            if (post!=null){
//                                myPostsList.add(post)
//                            }
//                        }
//                        recyclerView.adapter=MyPostsAdapter(myPostsList)
//                    }
//                }
//
//                .addOnFailureListener { exception ->
//                    Toast.makeText(this, "Error fetching posts: ${exception.message}", Toast.LENGTH_SHORT).show()
//                }
//        }





//        val userId=FirebaseAuth.getInstance().currentUser!!.uid
//        if (userId!=null){
//       firestore.collection("post")
//           .whereEqualTo("userId",userId)
//           .get()
//        .addOnSuccessListener {
//            querySnapshot->
//            for (document in querySnapshot.documents){
//                val posttitle=document.getString("postTitle")
//                val postcontent=document.getString("postContent")
//
//                myPostTitle.text=posttitle
//                myPostContent.text=postcontent
//
//            }}
//
//    }

//
//        editPostButton.setOnClickListener {
//            startActivity(Intent(this,EditMyPostActivity::class.java))
//        }





//    private fun getMyPosts(){
//
//    }
