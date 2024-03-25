package com.example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthzensignuplogin.R



/**
 * A simple [Fragment] subclass.
 * Use the [StressReliefFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StressReliefFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stress_relief, container, false)

//        val toEducationButton=view.findViewById<Button>(R.id.toEucation_button)

//        toEducationButton.setOnClickListener {
//            findNavController().navigate(R.id.Education)
//        }

        // Inflate the layout for this fragment


        return view
    }


}