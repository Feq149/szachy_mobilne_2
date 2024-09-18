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
) {
    override fun toString(): String {
        val stringBuilder = StringBuilder()
        if(userIsWhite) {
            stringBuilder.append("You vs $opponentName, ")
        } else {
            stringBuilder.append("$opponentName vs you, ")
        }
        if((result == 1 && userIsWhite) || (result == 0 && !userIsWhite) ) {
            stringBuilder.append("1 - 0")
        } else if(result == 0) {
            stringBuilder.append("1/2 - 1/2")
        } else {
            stringBuilder.append("0 - 1")
        }
        return stringBuilder.toString()
    }
}


