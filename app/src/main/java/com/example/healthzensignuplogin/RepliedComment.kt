package com.example.healthzensignuplogin

data class RepliedComment(
    val author: String, // Adding the author property
    val content: String,
    val repliedAuthorId: String,
    val date: String

)
