package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val newsButton: Button = view.findViewById(R.id.newsButton)
        val educationButton: Button = view.findViewById(R.id.educationButton)

        educationButton.setOnClickListener {
            // Launch NewsActivity when newsButton is clicked
            startActivity(Intent(activity, EducationActivity::class.java))
        }

        // Set click listener for the newsButton
        newsButton.setOnClickListener {
            // Launch NewsActivity when newsButton is clicked
            startActivity(Intent(activity, SplashActivity::class.java))
        }

        return view
    }
}