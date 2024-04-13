package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stressrelief.StressReliefActivity

class SplashStressReliefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_stress_relief)

        android.os.Handler().postDelayed({
            startActivity(Intent(this, StressReliefActivity::class.java))
            finish() // Finish SplashActivity to prevent user from returning to it
        }, 2000)
    }
}