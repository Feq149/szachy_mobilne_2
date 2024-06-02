package com.example.szachy_mobilne_2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase


@Database(
    version = 1,
    entities = [GameDb::class]
)
abstract class DatabaseOfGames : RoomDatabase() {


    abstract val dao : DatabaseWriter


    companion object {
        @Volatile
        var INSTANCE : DatabaseOfGames? = null
        fun getDatabase(context: Context): DatabaseOfGames {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseOfGames::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
