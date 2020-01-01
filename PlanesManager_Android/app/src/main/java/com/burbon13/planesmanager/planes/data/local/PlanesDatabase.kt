package com.burbon13.planesmanager.planes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.burbon13.planesmanager.planes.model.Plane


@Database(entities = [Plane::class], version = 1)
abstract class PlanesDatabase : RoomDatabase() {
    abstract fun planeDao(): PlaneDao

    companion object {
        @Volatile
        private var INSTANCE: PlanesDatabase? = null
        private const val DATABASE_NAME = "planes_db"

        fun getDatabase(context: Context): PlanesDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    PlanesDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
            INSTANCE = instance
            return instance
        }
    }
}
