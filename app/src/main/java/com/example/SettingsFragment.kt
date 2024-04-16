package com.example

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthzensignuplogin.CommunityPostDetailActivity
import com.example.healthzensignuplogin.MyPostDataClass
import com.example.healthzensignuplogin.MyPostsAdapter
import com.example.healthzensignuplogin.PostActivity
import com.example.healthzensignuplogin.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.education.SplashEducationActivity
import com.example.healthzensignuplogin.AllPostsActivity
import com.example.healthzensignuplogin.MyPostsActivity
import com.example.healthzensignuplogin.SplashActivity
import com.example.healthzensignuplogin.SplashCalendarActivity
import com.example.healthzensignuplogin.SplashStressReliefActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class SettingsFragment : Fragment() {


    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)


        val CommunityButton: Button = view.findViewById(R.id.CommunityButton)
        val AddPostButton: Button = view.findViewById(R.id.AddPostButton)
        val MyPostsButton: Button = view.findViewById(R.id.MyPostsButton)
        val NotificationButton: Button = view.findViewById(R.id.NotificationButton)

        CommunityButton.setOnClickListener {
            startActivity(Intent(activity, AllPostsActivity::class.java))
        }

        AddPostButton.setOnClickListener {
            startActivity(Intent(activity, PostActivity::class.java))
        }

        MyPostsButton.setOnClickListener {
            startActivity(Intent(activity, MyPostsActivity::class.java))
        }

        NotificationButton.setOnClickListener {
            startActivity(Intent(activity, SplashStressReliefActivity::class.java))
        }



        return view
    }
}

