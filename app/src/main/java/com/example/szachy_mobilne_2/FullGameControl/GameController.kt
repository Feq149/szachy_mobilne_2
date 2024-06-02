package com.example.szachy_mobilne_2.FullGameControl

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import com.example.szachy_mobile.Game
import com.example.szachy_mobilne_2.MainActivity
import com.example.szachy_mobilne_2.View.ChessView
import com.example.szachy_mobilne_2.database
import com.example.szachy_mobilne_2.database.GameDb
import com.example.szachy_mobilne_2.main_menu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Timer

import java.util.logging.Handler
import kotlin.concurrent.timerTask

class GameController(chessView: ChessView) {

    val game = Game()
    val userIsWhite = listOf(true,false).random()
    val chessView : ChessView
    var gameHistory = ""
    val date = java.util.Date()
    init{
        this.chessView = chessView
        this.chessView.gameController = this
        this.chessView.enableMove = this.userIsWhite
        if(!userIsWhite) {
            this.chessView.invalidate()
            getOpponentMovePlayed()
        }


    }

    fun performFinishingOperations() {
        if(!game.isGameFinished){
            return
        }
        var gameResult = 0
        if(game.isDraw()) {
            gameResult = 0
        }
        else if((game.didWhiteWin() && userIsWhite)||(!game.didWhiteWin() && !userIsWhite)) {
            gameResult = 1
        } else {
            gameResult = -1
        }
        val gameDb = GameDb(gameResult,userIsWhite,gameHistory,date.toString())
        GlobalScope.launch{
            database?.dao?.upsertGame(gameDb)
        }


    }

    fun getOpponentMovePlayed() {
        if(game.isGameFinished) {
            return
        }
        Timer().schedule(timerTask {
            val moveWasMade = game.makeAMove(game.getLegalMoves(!userIsWhite).random())
            if(!game.isGameFinished) {
                chessView.enableMove = true
            }
            chessView.invalidate()
            if(moveWasMade && game.isGameFinished) {
                performFinishingOperations()
            }

        }, 2000)

    }
    fun moveToStringConverter(move: Pair<Pair<Int,Int>,Pair<Int,Int>>) : String {
        var res = ""
        res += move.first.first.toString()
        res += " "

        res += move.first.second.toString()
        res += " -> "

        res += move.second.first.toString()
        res += " "

        res += move.second.second.toString()
        res += " ;"
        return res
    }

    fun makeAMove(move: Pair<Pair<Int,Int>,Pair<Int,Int>>) :Boolean{
        if(game.makeAMove(move)) {
            gameHistory += moveToStringConverter(move)
            if(game.isGameFinished) {
                performFinishingOperations()
            }
            return true
        }
        return false


    }



}