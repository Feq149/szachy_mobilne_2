package com.example.szachy_mobilne_2.FullGameControl

import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.szachy_mobile.Game
import com.example.szachy_mobilne_2.View.ChessView
import com.example.szachy_mobilne_2.database
import com.example.szachy_mobilne_2.database.GameDb
import kotlinx.coroutines.launch
import java.util.Timer

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
        var resultOfGameFromPlayersPerspective = 0
        if(game.isDraw()) {
            resultOfGameFromPlayersPerspective = 0
        }
        else if((game.didWhiteWin() && userIsWhite)||(!game.didWhiteWin() && !userIsWhite)) {
            resultOfGameFromPlayersPerspective = 1
        } else {
            resultOfGameFromPlayersPerspective = -1
        }
        val gameDb = GameDb(resultOfGameFromPlayersPerspective,userIsWhite,gameHistory,date.toString())
        chessView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            database?.dao?.upsertGame(gameDb)
        }
        chessView.showResultPopup(resultOfGameFromPlayersPerspective);




    }

    fun getOpponentMovePlayed() {
        if(game.isGameFinished) {
            return
        }
        Timer().schedule(timerTask {
            val move = game.getLegalMoves(!userIsWhite).random()
            val moveWasMade = game.makeAMove(move)
            if(!game.isGameFinished) {
                chessView.enableMove = true
            }
            chessView.invalidate()

            if(moveWasMade) {
                gameHistory += moveToStringConverter(move)
                if(game.isGameFinished) {
                    performFinishingOperations()
                }
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