package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ProfileFragment
import com.example.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CommunityActivity : AppCompatActivity() {
    private  lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var goPostButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        bottomNavigationView=findViewById(R.id.bottom_navigation)




        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    startFragment(HomeFragment())
                    true
                }
                R.id.bottom_profile -> {
                    startFragment(ProfileFragment())
                    true
                }
                R.id.bottom_settings -> {
                    startFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }




        goPostButton=findViewById(R.id.goPostButton)


        goPostButton.setOnClickListener {
            startActivity(Intent(this,PostActivity::class.java))
        }
    }



    private fun startFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }

}