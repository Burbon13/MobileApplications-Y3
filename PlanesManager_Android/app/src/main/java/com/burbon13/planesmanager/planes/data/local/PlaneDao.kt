package com.burbon13.planesmanager.planes.data.local

import androidx.lifecycle.LiveData
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.burbon13.planesmanager.planes.model.Plane


@Dao
interface PlaneDao {
    @Query("SELECT * FROM planes ORDER BY tailNumber ASC")
    fun getAll(): LiveData<List<Plane>>

    @Query("SELECT * FROM planes WHERE tailNumber=:tailNumber")
    fun getByTailNumber(tailNumber: String): Plane

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plane: Plane)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(plane: Plane)

    @Query("DELETE FROM planes WHERE tailNumber=:tailNumber")
    suspend fun delete(tailNumber: String)

    @Query("DELETE FROM planes")
    suspend fun deleteAll()

    @Query("SELECT * FROM planes ORDER BY tailNumber ASC LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<Plane>

    @Query("SELECT COUNT(*) FROM planes WHERE brand=:brand")
    suspend fun getBrandCount(brand: String): Int
}
