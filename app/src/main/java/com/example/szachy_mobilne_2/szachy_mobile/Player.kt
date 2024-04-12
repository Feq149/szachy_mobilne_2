package com.example.szachy_mobile

class Player (game:Game, isWhite:Boolean){
    val game:Game
    val isWhite:Boolean
    init{
        this.game = game
        this.isWhite = isWhite
    }
    fun playMove() :Boolean{
        if(game.isGameFinished || game.whiteToMove != isWhite) {return false}
        val moves = game.getLegalMoves(isWhite)
        val move = moves.random()
        return game.makeAMove(move)
    }
}