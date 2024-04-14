package com.example.stressrelief

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthzensignuplogin.R

class StressReliefActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_video_sr)

        val recyclerView: RecyclerView = findViewById(R.id.rv_video)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = VideoAdapterSR()
        recyclerView.adapter = adapter

        // Fetch stress relief videos here and pass them to the adapter
        val viewModel = ViewModelProvider(this).get(VideoViewModelSR::class.java)
        viewModel.video.observe(this) { video ->
            video?.let {
                adapter.setData(it.items, recyclerView)
            }
        }
        viewModel.getVideoListSR()
    }
}

