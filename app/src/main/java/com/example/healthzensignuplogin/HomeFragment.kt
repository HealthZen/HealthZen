package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.education.EducationActivity


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val newsButton: Button = view.findViewById(R.id.newsButton)
        val educationButton: Button = view.findViewById(R.id.educationButton)
        val calendarButton: Button = view.findViewById(R.id.calendarButton)
        val chatButton: Button = view.findViewById(R.id.chatButton)

        calendarButton.setOnClickListener {
            // Launch NewsActivity when newsButton is clicked
            startActivity(Intent(activity, CalendarActivity::class.java))
        }

        educationButton.setOnClickListener {
            // Launch NewsActivity when newsButton is clicked
            startActivity(Intent(activity, EducationActivity::class.java))
        }

        // Set click listener for the newsButton
        newsButton.setOnClickListener {
            // Launch NewsActivity when newsButton is clicked
            startActivity(Intent(activity, SplashActivity::class.java))
        }

        chatButton.setOnClickListener {
            // Launch NewsActivity when newsButton is clicked
            startActivity(Intent(activity,AllPostsActivity::class.java))
        }

        return view
    }
}