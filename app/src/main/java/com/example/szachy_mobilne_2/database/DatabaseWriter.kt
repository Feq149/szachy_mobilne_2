package com.example.szachy_mobilne_2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseWriter {
    @Upsert
    suspend fun upsertGame(databaseGame: GameDb)
    @Delete
    suspend fun deleteGame(databaseGame: GameDb)

    @Query("SELECT * FROM GameDb ORDER BY date DESC")
    fun getGamesOrderedByDate() : List<GameDb>

}