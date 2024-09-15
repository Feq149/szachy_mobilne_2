package com.example.szachy_mobilne_2.FullGameControl

import com.example.szachy_mobile.Board
import com.example.szachy_mobilne_2.database.GameDb

class GameReplayController(gameDb: GameDb) {
    private var currentMove = 0
    val userIsWhite : Boolean
    private val moves : List<Pair<Pair<Int,Int>,Pair<Int,Int>>>
    init{

        moves = getMovesFromStringRepresentingMoves(gameDb.game)
        this.userIsWhite = gameDb.userIsWhite
    }

    private fun getMovesFromStringRepresentingMoves(movesString : String) : List<Pair<Pair<Int,Int>,Pair<Int,Int>>> {
        val listOfIndividualStringMoves = movesString.split(";")
        val uselessMove = Pair(Pair(1,2),Pair(3,4))
        val result = mutableListOf(uselessMove)
        result.removeAt(0)
        for(i in 0..listOfIndividualStringMoves.size) {
            val moveNoSpaces = listOfIndividualStringMoves[i].split(" ")
            val move = Pair(Pair(moveNoSpaces[0].toInt(),moveNoSpaces[1].toInt()),Pair(moveNoSpaces[4].toInt(),moveNoSpaces[5].toInt()))
            result.add(move)
        }
        return result
    }
    fun getCurrentPosition():Board {
        val result = Board()
        for(i in 0..currentMove) {
            result.movePiece(moves[i].first,moves[i].second)
        }
        return result
    }
    fun turnToNextMoveAndGetThePosition():Board {
        if(currentMove < moves.size) {
            currentMove += 1
        }
        return getCurrentPosition()
    }
    fun turnToPreviousMoveAndGetThePosition() :Board{
        if(currentMove > 0) {
            currentMove -= 1
        }
        return getCurrentPosition()
    }



}