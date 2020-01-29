package com.burbon13.app.data.model

import androidx.room.*


@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(message: Message)
}