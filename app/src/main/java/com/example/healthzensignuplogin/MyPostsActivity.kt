package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPostsActivity : AppCompatActivity() {

    private lateinit var myPostTitle:TextView
    private lateinit var myPostContent:TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var editPostButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        myPostTitle=findViewById(R.id.myPostTitle)
        myPostContent=findViewById(R.id.myPostContent)
        auth= FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()
        editPostButton=findViewById(R.id.editPostButton)


        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        if (userId!=null){
       firestore.collection("post")
           .whereEqualTo("userId",userId)
           .get()
        .addOnSuccessListener {
            querySnapshot->
            for (document in querySnapshot.documents){
                val posttitle=document.getString("postTitle")
                val postcontent=document.getString("postContent")

                myPostTitle.text=posttitle
                myPostContent.text=postcontent

            }}

    }


        editPostButton.setOnClickListener {
            startActivity(Intent(this,EditMyPostActivity::class.java))
        }


    }
}