package com.burbon13.template.myobjects.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.burbon13.template.myobjects.model.MyObject


@Dao
interface MyObjectDao {
    //    @Query("SELECT * FROM myObjects ORDER BY count ASC")
    @Query("SELECT * FROM myObjects")
    fun getAll(): LiveData<List<MyObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(myObject: MyObject)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(myObject: MyObject)

    @Query("DELETE FROM myObjects WHERE id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM myObjects")
    suspend fun deleteAll()

    @Query("SELECT * FROM myObjects ORDER BY count ASC LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<MyObject>

    @Query("SELECT COUNT(*) FROM myObjects WHERE name=:name")
    suspend fun getBrandCount(name: String): Int
}
