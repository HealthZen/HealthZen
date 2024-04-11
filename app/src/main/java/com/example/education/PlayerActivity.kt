package com.example.education

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthzensignuplogin.R

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val webView = findViewById<WebView>(R.id.web_view)

        // Get the video ID from the intent extra
        val videoId = intent.getStringExtra("video_id")

        // Generate the YouTube embed URL using the video ID
        val videoUrl = "https://www.youtube.com/embed/$videoId"

        // Load the generated URL into the WebView
        webView.loadData(
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>",
            "text/html",
            "utf-8"
        )
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
    }
}
