package com.example.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Thumbnail(
    @SerializedName("high")
    val high: High
) {
    data class High(
        @SerializedName("url")
        val url: String
    )
}