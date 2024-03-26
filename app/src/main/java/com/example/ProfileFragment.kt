package com.example

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.healthzensignuplogin.R

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView

    // Add variables to store user data
    private var nameUser: String? = null
    private var emailUser: String? = null
    private var usernameUser: String? = null
    private var passwordUser: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        profileUsername = view.findViewById(R.id.profileUsername)
        profilePassword = view.findViewById(R.id.profilePassword)
        titleName = view.findViewById(R.id.titleName)
        titleUsername = view.findViewById(R.id.titleUsername)

        val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", "")
        val email = sharedPref.getString("email", "")
        val username = sharedPref.getString("username", "")
        val password = sharedPref.getString("password", "")

        // Set user data to TextViews
        setUserData(name, email, username, password)
    }

    // Function to set user data
    fun setUserData(name: String?, email: String?, username: String?, password: String?) {
        nameUser = name
        emailUser = email
        usernameUser = username
        passwordUser = password

        // Update UI with user data
        showUserData()
    }

    // Function to update UI with user data
    private fun showUserData() {
        // Check if TextViews are initialized
        if (::titleName.isInitialized && ::titleUsername.isInitialized &&
            ::profileName.isInitialized && ::profileEmail.isInitialized &&
            ::profileUsername.isInitialized && ::profilePassword.isInitialized) {
            // Set user data to TextViews
            titleName.text = nameUser
            titleUsername.text = usernameUser
            profileName.text = nameUser
            profileEmail.text = emailUser
            profileUsername.text = usernameUser
            profilePassword.text = passwordUser
        }
    }
}
