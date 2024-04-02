package com.example.healthzensignuplogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ProfileFragment
import com.example.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private  lateinit var bottomNavigationView: BottomNavigationView
    //private lateinit var educationButton: Button
    //private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { menuItem->
            when (menuItem.itemId){
                R.id.bottom_home->{
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_profile->{
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.bottom_settings->{
                    replaceFragment(SettingsFragment())
                    true
                }
                else ->false
            }

        }
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }

}