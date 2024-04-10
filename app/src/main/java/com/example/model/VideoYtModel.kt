package com.example.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VideoYtModel(
    @SerializedName("nextPageToken")
    val nextPageToken: String?,

    @SerializedName("items")
    val items: List<VideoItem>

) {

    data class VideoItem (
        @SerializedName("id")
        val videoId: VideoId,

        @SerializedName("snippet")
        val snippetYt: Snippet
    )

    data class VideoId(
        @SerializedName("videoId")
        val id: String
    )

}