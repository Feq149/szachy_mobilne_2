package com.example.szachy_mobile

class Knight(name: String, isWhite: Boolean, board: Board) : Piece(name, isWhite,board) {
    override fun getAvailableSquares(startingSquare: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int,Int>>()
        val i = startingSquare.first
        val j = startingSquare.second
        if(i > 1 && j > 0 ) {
            res.add(Pair(i - 2,j - 1))
        }
        if(i > 0 && j > 1 ) {
            res.add(Pair(i - 1,j - 2))
        }
        if(i > 1 && j < 7 ) {
            res.add(Pair(i - 2,j + 1))
        }
        if(i > 0 && j < 6 ) {
            res.add(Pair(i - 1,j + 2))
        }
        if(i < 6 && j > 0 ) {
            res.add(Pair(i + 2,j - 1))
        }
        if(i < 7 && j > 1) {
            res.add(Pair(i + 1,j - 2))
        }
        if(i < 7 && j < 6 ) {
            res.add(Pair(i + 1,j + 2))
        }
        if(i < 6 && j < 7) {
            res.add(Pair(i + 2,j + 1))
        }
        return res
    }

    override fun toString(): String {
        if(isWhite) {
            return "n"
        }
        return "N"
    }
}