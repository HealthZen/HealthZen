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

    private lateinit var editPostTitle:EditText
    private lateinit var editPostContent:EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var submitButton:Button
    private lateinit var cancelButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_post)


        editPostTitle=findViewById(R.id.editPostTitle)
        editPostContent=findViewById(R.id.editPostContent)
        auth= FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()
        submitButton=findViewById(R.id.submitButton)
        cancelButton=findViewById(R.id.cancelButton)


        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        val ref=firestore.collection("post").document(userId)
        ref.get().addOnSuccessListener {
            if (it!=null) {
                val posttitle = it.data?.get("postTitle")?.toString()
                val postcontent = it.data?.get("postContent")?.toString()

                editPostTitle.text=Editable.Factory.getInstance().newEditable(posttitle)
           editPostContent.text=Editable.Factory.getInstance().newEditable(postcontent)

                editPostTitle.isEnabled=true
                editPostContent.isEnabled=true


                submitButton.setOnClickListener {

                    if (editPostContent.text.toString().isNotEmpty()&&editPostTitle.text.toString().isNotEmpty()) {

                        val currentUser=auth.currentUser
                        if (currentUser!=null){
                            val userId=auth.currentUser!!.uid


                            val editPostContent=editPostContent.text.toString()
                            val editPostTitle=editPostTitle.text.toString()

                            val updateMap= mapOf(
                                "postTitle" to editPostTitle,
                                "postContent" to editPostContent

                            )
                            firestore.collection("post").document(userId)
                                .update(updateMap)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@EditMyPostActivity,
                                        "edited post successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this,MyPostsActivity::class.java))

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@EditMyPostActivity,
                                        "Failed to edit post:${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }else{
                            Toast.makeText(
                                this@EditMyPostActivity,
                                "User not logged in"
                                ,
                                Toast.LENGTH_SHORT
                            ).show()
                        }}
                    else {
                        Toast.makeText(
                            this@EditMyPostActivity,
                            "field cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }



                }

                cancelButton.setOnClickListener {
                    startActivity(Intent(this,MyPostsActivity::class.java))
                }




            }

    }




}


}