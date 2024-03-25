package com.example.healthzensignuplogin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.EducationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.fragment.findNavController
import com.example.StressReliefFragment


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val toEducationButton=view.findViewById<Button>(R.id.toEucation_button)

        val toSressReliefButton=view.findViewById<Button>(R.id.toStressRelief_button)

        toEducationButton.setOnClickListener {
            // Navigate to the EducationFragment when the button is clicked
            navigateToEducationFragment()
        }

        toSressReliefButton.setOnClickListener {
            // Navigate to the EducationFragment when the button is clicked
            navigateStressReliefFragment()
        }

//        toEducationButton.setOnClickListener {
//            findNavController().navigate(R.id.Education)
//        }

        // Inflate the layout for this fragment


        return view
    }

    private fun navigateToEducationFragment() {
        // Create instance of EducationFragment
        val educationFragment = EducationFragment()

        // Get FragmentManager
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // Start a FragmentTransaction
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frame_container, educationFragment)
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }

    private fun navigateStressReliefFragment() {
        // Create instance of EducationFragment
        val stressReliefFragment = StressReliefFragment()

        // Get FragmentManager
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // Start a FragmentTransaction
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.frame_container, stressReliefFragment)
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }

}