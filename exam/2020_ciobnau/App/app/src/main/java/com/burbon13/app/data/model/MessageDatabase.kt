package com.burbon13.app.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase: RoomDatabase() {
    abstract fun messaggeDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null
        private const val DATABASE_NAME = "msg_sb"

        fun getDatabase(context: Context): MessageDatabase {
            val inst = INSTANCE
            if (inst != null) {
                return inst
            }
            val instance =
                Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
            INSTANCE = instance
            return instance
        }
    }
}
