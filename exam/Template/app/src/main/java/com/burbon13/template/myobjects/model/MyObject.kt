package com.burbon13.template.myobjects.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "myObjects")
data class MyObject(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "count")
    val count: Int,

    @ColumnInfo(name = "val3")
    val val3: String,

    @ColumnInfo(name = "val4")
    val val4: String
)
