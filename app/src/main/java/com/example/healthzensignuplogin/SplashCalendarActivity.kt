package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_calendar)

        android.os.Handler().postDelayed({
            startActivity(Intent(this, CalendarActivity::class.java))
            finish() // Finish SplashActivity to prevent user from returning to it
        }, 3000)
    }
}