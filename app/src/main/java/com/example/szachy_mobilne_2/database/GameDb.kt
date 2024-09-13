package com.example.szachy_mobilne_2.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date

@Entity
data class GameDb(var result:Int = 0,
                  var userIsWhite :Boolean = true,
                  var game :String = "", var date : String,
                  @PrimaryKey(autoGenerate = true)
                  var id: Int = 0,
                  var opponentName: String = "random"
)


