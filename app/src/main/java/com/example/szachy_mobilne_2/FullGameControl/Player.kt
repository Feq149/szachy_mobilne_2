package com.example.szachy_mobilne_2.FullGameControl

class Player {
    fun getMove(legalMoves : List<Pair<Pair<Int,Int>,Pair<Int,Int>>>) : Pair<Pair<Int,Int>,Pair<Int,Int>> {
        Thread.sleep(3000)
        return legalMoves.random()
    }
}