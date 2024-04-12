package com.example.szachy_mobile

class King(name: String, isWhite: Boolean, board: Board) : Piece(name, isWhite,board) {

    override fun getAvailableSquares(startingSquare: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
        val res = mutableListOf<Pair<Int,Int>>()
        val i = startingSquare.first
        val j = startingSquare.second
        if(i > 0) {
            res.add(Pair(i - 1,j))
            if(j > 0) {
                res.add(Pair(i - 1,j - 1))
            }
            if(j < 7) {
                res.add(Pair(i-1,j+1))
            }
        }
        if(i < 7) {
            res.add(Pair(i + 1,j))
            if(j > 0) {
                res.add(Pair(i + 1,j - 1))
            }
            if(j < 7) {
                res.add(Pair(i+1,j+1))
            }
        }
        if(j > 0) {
            res.add(Pair(i,j - 1))
        }
        if(j < 7) {
            res.add(Pair(i,j+1))
        }
        return res
    }
    override fun toString(): String {
        if(isWhite) {
            return "k"
        }
        return "K"
    }

}