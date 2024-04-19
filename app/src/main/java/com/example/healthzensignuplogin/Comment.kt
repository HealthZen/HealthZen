package com.example.healthzensignuplogin

data class Comment(
    val commentAuthorId:String,
    val content: String,
    val date: String,
    val commentId: String,
    val postId:String,
    val author: String,
    val repliedComments: List<RepliedComment>
)
