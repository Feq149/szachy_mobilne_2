package com.example.szachy_mobile

import com.example.szachy_mobilne_2.FullGameControl.GameController

open class Player (gameController:GameController, isWhite:Boolean){
    val gameController:GameController
    val isWhite:Boolean
    init{
        this.gameController = gameController
        this.isWhite = isWhite
    }
    open fun playMove() :Boolean{
        if(gameController.game.isGameFinished || gameController.game.whiteToMove != isWhite) {return false}
        val moves = gameController.game.getLegalMoves(isWhite)
        val move = moves.random()
        return gameController.makeAMove(move)
    }
}