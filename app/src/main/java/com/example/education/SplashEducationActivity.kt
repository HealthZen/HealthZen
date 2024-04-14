package com.example.education

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthzensignuplogin.CalendarActivity
import com.example.healthzensignuplogin.R

class SplashEducationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_education)

        android.os.Handler().postDelayed({
            startActivity(Intent(this, EducationActivity::class.java))
            finish() // Finish SplashActivity to prevent user from returning to it
        }, 3000)
    }
}