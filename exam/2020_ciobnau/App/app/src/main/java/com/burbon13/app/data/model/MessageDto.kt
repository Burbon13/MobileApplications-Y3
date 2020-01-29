package com.burbon13.app.data.model

data class MessageDto(
    val sender: String,
    var unread: Int,
    var lastUnread: Long,
    val messages: ArrayList<Message>
)