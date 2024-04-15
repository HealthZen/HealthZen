package com.example.meditation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthzensignuplogin.R
import com.example.stressrelief.StressReliefActivity

class SplashMeditationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_meditation)

        android.os.Handler().postDelayed({
            startActivity(Intent(this, MeditationActivity::class.java))
            finish() // Finish SplashActivity to prevent user from returning to it
        }, 2000)
    }
}