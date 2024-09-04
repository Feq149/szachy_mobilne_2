package com.example.szachy_mobilne_2.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    version = 2,
    entities = [GameDb::class],


)
abstract class DatabaseOfGames : RoomDatabase() {




    abstract val dao : DatabaseWriter


    companion object {
        @Volatile
        var INSTANCE : DatabaseOfGames? = null
        fun getDatabase(context: Context): DatabaseOfGames {
            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                        "ALTER TABLE GameDb ADD COLUMN opponentName TEXT DEFAULT random NOT NULL"
                    )
                }
            }
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseOfGames::class.java,
                    "note_database"
                ).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
