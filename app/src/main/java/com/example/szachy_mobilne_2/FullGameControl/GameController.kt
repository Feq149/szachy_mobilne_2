package com.example.szachy_mobilne_2.FullGameControl

import com.example.szachy_mobile.Game
import com.example.szachy_mobilne_2.View.ChessView

class GameController(chessView: ChessView) {
    val userPlayer = Player()
    val opponentPlayer = Player()
    val game = Game()
    val userIsWhite = true
    val chessView : ChessView
    init{
        this.chessView = chessView
        this.chessView.gameController = this
    }

    fun playTheGame() {

    }

    fun makeAMove(move: Pair<Pair<Int,Int>,Pair<Int,Int>>) {
        val isMoveSuccessfull = game.makeAMove(move)
        if(isMoveSuccessfull) {
            chessView.invalidate()
        }
    }



}