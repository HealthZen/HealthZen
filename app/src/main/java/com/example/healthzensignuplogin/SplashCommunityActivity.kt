package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.SettingsFragment
import com.example.stressrelief.StressReliefActivity
import com.google.firebase.database.Transaction
import java.util.logging.Handler

class SplashCommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_community)

        // Use a Handler to delay the fragment replacement
        android.os.Handler().postDelayed({
            // Replace the current fragment with the SettingsFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.community_container, SettingsFragment())
                .commit()
        }, 2000) // 2000 milliseconds (2 seconds) delay
    }
}
