package com.burbon13.planesmanager.planes.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.burbon13.planesmanager.planes.model.Plane


@Dao
interface PlaneDao {
    @Query("SELECT * FROM planes ORDER BY tailNumber ASC")
    fun getAll(): LiveData<List<Plane>>

    @Query("SELECT * FROM planes WHERE tailNumber=:tailNumber")
    fun getByTailNumber(tailNumber: String): LiveData<Plane>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plane: Plane)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(plane: Plane)

    @Delete
    suspend fun delete(plane: Plane)

    @Query("DELETE FROM planes")
    suspend fun deleteAll()
}
