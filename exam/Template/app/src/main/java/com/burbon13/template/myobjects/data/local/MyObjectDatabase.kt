package com.burbon13.template.myobjects.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.burbon13.template.myobjects.model.MyObject


@Database(entities = [MyObject::class], version = 1)
abstract class MyObjectDatabase : RoomDatabase() {
    abstract fun myObjectDao(): MyObjectDao

    companion object {
        @Volatile
        private var INSTANCE: MyObjectDatabase? = null
        private const val DATABASE_NAME = "my_template_db"

        fun getDatabase(context: Context): MyObjectDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    MyObjectDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
            INSTANCE = instance
            return instance
        }
    }
}
