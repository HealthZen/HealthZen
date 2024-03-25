package com.example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController

import com.example.healthzensignuplogin.R



/**
 * A simple [Fragment] subclass.
 * Use the [BreathingExercisesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BreathingExercisesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_breathing_exercises, container, false)
        val button_arrow_back=view.findViewById<Button>(R.id.button_arrow_back)


        button_arrow_back.setOnClickListener {
            backToStressReliefFragment()
        }


//        toEducationButton.setOnClickListener {
//            findNavController().navigate(R.id.Education)
//        }

        // Inflate the layout for this fragment


        return view
    }   private fun backToStressReliefFragment() {
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
    }}
