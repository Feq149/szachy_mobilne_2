package com.example.szachy_mobilne_2.FullGameControl

import com.example.szachy_mobile.Game
import com.example.szachy_mobilne_2.View.ChessView
import java.util.Timer
import java.util.logging.Handler
import kotlin.concurrent.timerTask

class GameController(chessView: ChessView) {

    val game = Game()
    val userIsWhite = true
    val chessView : ChessView
    init{
        this.chessView = chessView
        this.chessView.gameController = this

    }

    fun getOpponentMovePlayed() {
        if(game.isGameFinished) {
            return
        }
        Timer().schedule(timerTask {
            game.makeAMove(game.getLegalMoves(!userIsWhite).random())
            chessView.invalidate()
            chessView.enableMove = true
        }, 2000)

    }

    fun makeAMove(move: Pair<Pair<Int,Int>,Pair<Int,Int>>) :Boolean{
        return  game.makeAMove(move)

    }



}