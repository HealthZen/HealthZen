package com.example

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthzensignuplogin.PostActivity
import com.example.healthzensignuplogin.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.healthzensignuplogin.AllPostsActivity
import com.example.healthzensignuplogin.FavoritesActivity
import com.example.healthzensignuplogin.MyPostsActivity
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
        val favoritesButton: Button=view.findViewById(R.id.favoritesButton)
        CommunityButton.setOnClickListener {
            startActivity(Intent(activity, AllPostsActivity::class.java))
        }

        AddPostButton.setOnClickListener {
            startActivity(Intent(activity, PostActivity::class.java))
        }

        MyPostsButton.setOnClickListener {
            startActivity(Intent(activity, MyPostsActivity::class.java))
        }

        favoritesButton.setOnClickListener {
            startActivity(Intent(activity, FavoritesActivity::class.java))
        }
        NotificationButton.setOnClickListener {
            startActivity(Intent(activity, SplashStressReliefActivity::class.java))
        }



        return view
    }
}

