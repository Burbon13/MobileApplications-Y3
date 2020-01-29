package com.burbon13.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "message")
data class Message(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "text")
    val text: String,

    @ColumnInfo(name = "read")
    var read: Boolean,

    @ColumnInfo(name = "sender")
    val sender: String,

    @ColumnInfo(name = "created")
    val created: Long
)
