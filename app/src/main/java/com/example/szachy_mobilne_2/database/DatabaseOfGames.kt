package com.example.szachy_mobilne_2.database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    version = 1,
    entities = [GameDb::class]
)
abstract class DatabaseOfGames : RoomDatabase() {

    abstract val dao : DatabaseWriter
}