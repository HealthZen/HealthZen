package com.example.healthzensignuplogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditMyPostActivity : AppCompatActivity() {

    private lateinit var editPostTitle:EditText
    private lateinit var editPostContent:EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_my_post)


        editPostTitle=findViewById(R.id.editPostTitle)
        editPostContent=findViewById(R.id.editPostContent)
        auth= FirebaseAuth.getInstance()
        firestore=FirebaseFirestore.getInstance()


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

            }


    }
}


}