package com.example.szachy_mobile

open class Piece(name:String,isWhite:Boolean, board:Board) {
    val name:String
    val isWhite:Boolean
    val board:Board
    open fun getAvailableSquares(startingSquare:Pair<Int,Int>) :MutableList<Pair<Int,Int>>{
        return mutableListOf()
    }
    init{
        this.name = name
        this.isWhite = isWhite
        this.board = board

    }
    override fun toString(): String {

        return "."
    }
}