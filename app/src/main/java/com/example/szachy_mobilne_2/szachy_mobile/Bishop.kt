package com.example.szachy_mobile

class Bishop(name: String, isWhite: Boolean, board: Board) : Piece(name, isWhite,board) {
    override fun getAvailableSquares(startingSquare: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
        var res = mutableListOf<Pair<Int,Int>>()
        var i = startingSquare.first - 1
        var j = startingSquare.second - 1
        while(i >= 0 && j >= 0) {
            res.add(Pair(i,j))
            i--
            j--
        }
        i = startingSquare.first + 1
        j = startingSquare.second - 1
        while(i <= 7 && j >= 0) {
            res.add(Pair(i,j))
            i++
            j--
        }
        i = startingSquare.first - 1
        j = startingSquare.second + 1
        while(i >=0 && j <= 7) {
            res.add(Pair(i,j))
            i--
            j++
        }
        i = startingSquare.first + 1
        j = startingSquare.second + 1
        while(i <= 7 && j <= 7) {
            res.add(Pair(i,j))
            i++
            j++
        }
        return res
    }
    override fun toString(): String {
        if(isWhite) {
            return "b"
        }
        return "B";
    }
}