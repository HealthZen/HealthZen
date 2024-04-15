package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.education.EducationActivity
import com.example.education.SplashEducationActivity
import com.example.meditation.SplashMeditationActivity


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
        val meditationButton: Button = view.findViewById(R.id.meditationButton)
        val stressReliefButton: Button = view.findViewById(R.id.stressReliefButton)
        val chatButton: Button = view.findViewById(R.id.chatButton)

        calendarButton.setOnClickListener {
            startActivity(Intent(activity, SplashCalendarActivity::class.java))
        }

        educationButton.setOnClickListener {
            startActivity(Intent(activity, SplashEducationActivity::class.java))
        }

        newsButton.setOnClickListener {
            startActivity(Intent(activity, SplashActivity::class.java))
        }

        stressReliefButton.setOnClickListener {
            startActivity(Intent(activity, SplashStressReliefActivity::class.java))
        }

        chatButton.setOnClickListener {
            startActivity(Intent(activity, AllPostsActivity::class.java))
        }

        meditationButton.setOnClickListener {
            startActivity(Intent(activity, SplashMeditationActivity::class.java))
        }

        return view
    }
}