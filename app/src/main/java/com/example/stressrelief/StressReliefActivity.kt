package com.example.stressrelief

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.healthzensignuplogin.R

class StressReliefActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stress_relief)

        // Fetch stress relief videos here
        val viewModel = ViewModelProvider(this)[VideoViewModelSR::class.java]
        viewModel.getVideoListSR()
    }
}
