package com.example.model

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class Snippet(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName ("customUrl")
    val customUrl: String,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("thumbnails")
    val thumbnails: Thumbnail,

    @SerializedName("country")
    val country: String
)
