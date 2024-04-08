package com.example.healthzensignuplogin

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PostActivity : AppCompatActivity() {


    private lateinit var postTitle:EditText
    private lateinit var postContent:EditText
    private lateinit var submitPostButton:Button
    private lateinit var firestore:FirebaseFirestore
    private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)


        postTitle = findViewById(R.id.postTitle)
        postContent = findViewById(R.id.postContent)
        submitPostButton = findViewById(R.id.submitPostButton)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()



        submitPostButton.setOnClickListener {
            var posttitle = postTitle.text.toString()
            var postcontent = postContent.text.toString()

            if (posttitle.isNotEmpty() && postcontent.isNotEmpty()) {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userId = currentUser!!.uid
                    val ref = firestore.collection("users").document(userId)
                    ref.get().addOnSuccessListener {
                        if (it != null) {
                            val username = it.data?.get("username")?.toString()


                            val post = hashMapOf(
                                "postTitle" to posttitle,
                                "postContent" to postcontent,
                                "poster" to username,
                            )
                            firestore.collection("post").document(userId)
                                .set(post)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@PostActivity,
                                        "post successfuuly",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this,CommunityActivity::class.java))

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@PostActivity,
                                        "Failed to change username:${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }

                }
            }
        }


    }}