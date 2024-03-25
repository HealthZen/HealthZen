package com.example

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.findNavController
import com.example.healthzensignuplogin.R


class EducationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_education, container, false)

//        val toEducationButton=view.findViewById<Button>(R.id.toEucation_button)

//        toEducationButton.setOnClickListener {
//            findNavController().navigate(R.id.Education)
//        }

        // Inflate the layout for this fragment


        return view
    }


}