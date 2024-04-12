package com.example.szachy_mobile

class Pawn(name: String, isWhite: Boolean, board: Board) : Piece(name, isWhite,board) {
    override fun getAvailableSquares(startingSquare:Pair<Int,Int>) :MutableList<Pair<Int,Int>>{
        val res = mutableListOf<Pair<Int,Int>>()
        if(isWhite) {
            val i = startingSquare.first - 1
            if(startingSquare.first == 6) {
                res.add(Pair(4,startingSquare.second))
            }
            if(i > 0) {
                res.add(Pair(i,startingSquare.second))
                var j = startingSquare.second - 1
                if(j >= 0) {
                    res.add(Pair(i,j))
                }
                j = startingSquare.second + 1
                if(j <= 7) {
                    res.add(Pair(i,j))
                }
            }
        } else {
            val i = startingSquare.first + 1
            if(startingSquare.first == 1) {
                res.add(Pair(3,startingSquare.second))
            }
            if(i < 8) {
                res.add(Pair(i,startingSquare.second))
                var j = startingSquare.second - 1
                if(j >= 0) {
                    res.add(Pair(i,j))
                }
                j = startingSquare.second + 1
                if(j <= 7) {
                    res.add(Pair(i,j))
                }
            }
        }
        return res
    }
    override fun toString(): String {
        if(isWhite) {
            return "p"
        }
        return "P"
    }

}