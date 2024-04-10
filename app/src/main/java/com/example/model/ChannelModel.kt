package com.example.model

import android.provider.ContactsContract.SearchSnippets
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class ChannelModel (

    @SerializedName("items")
    val items: List<Items>

) {
    data class Items(

        @SerializedName("id")
        val id: String,

        @SerializedName("snippet")
        val snippet: Snippet
    )
}
