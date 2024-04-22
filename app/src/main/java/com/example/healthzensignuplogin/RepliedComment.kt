package com.example.healthzensignuplogin

data class RepliedComment(
    val repliedAuthor: String, // Adding the author property
    val repliedContent: String,
    val repliedAuthorId: String,
    val repliedDate: String,
    val parentCommentId:String,
    val postId:String

)
