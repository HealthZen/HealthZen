package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CommunityActivity : AppCompatActivity() {

    private lateinit var goPostButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)


        goPostButton=findViewById(R.id.goPostButton)


        goPostButton.setOnClickListener {
            startActivity(Intent(this,PostActivity::class.java))
        }
    }



}