package com.example.szachy_mobile

class Rook(name: String, isWhite: Boolean, board: Board) : Piece(name, isWhite,board) {
    override fun getAvailableSquares(startingSquare: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int,Int>>()
        var i = startingSquare.first - 1
        var j = startingSquare.second
        while(i >= 0) {
            res.add(Pair(i,j))
            i--
        }
        i = startingSquare.first + 1
        j = startingSquare.second
        while(i <= 7) {
            res.add(Pair(i,j))
            i++
        }
        i = startingSquare.first
        j = startingSquare.second + 1
        while(j <= 7) {
            res.add(Pair(i,j))
            j++
        }
        i = startingSquare.first
        j = startingSquare.second - 1
        while(j >= 0) {
            res.add(Pair(i,j))
            j--
        }
        return res
    }
    override fun toString(): String {
        if(isWhite) {
            return "r"
        }
        return "R"
    }
}