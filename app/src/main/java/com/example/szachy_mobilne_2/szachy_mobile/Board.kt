package com.example.szachy_mobile

class Board {
    var rows = mutableListOf<MutableList<Piece>>()
    init {
        for(i in 0..7) {
            var toAdd = mutableListOf<Piece>()
            for(j in 0..7) {
                if (i == 0 || i == 7) {
                    val isWhite = !(i == 0)
                    if(j == 0 || j == 7) {
                        toAdd.add(Rook("rook", isWhite, this))
                    } else if(j == 1 || j == 6) {
                        toAdd.add(Knight("knight", isWhite, this))
                    } else if(j == 2 || j == 5) {
                        toAdd.add(Bishop("bishop", isWhite, this))
                    }
                    else if(j == 3) {
                        toAdd.add(Queen("queen", isWhite, this))
                    } else {
                        toAdd.add(King("king", isWhite, this))
                    }
                }
                else if(i == 1 || i == 6) {
                    toAdd.add(Pawn("pawn",i == 6,this))
                }
                else {
                    toAdd.add(EmptySquare(".", false, this))

                }
            }
            rows.add(toAdd)
        }
    }
    fun movePiece(from:Pair<Int,Int>,to:Pair<Int,Int>){
        rows[to.first][to.second] = rows[from.first][from.second]
        rows[from.first][from.second] = EmptySquare(".",false,this)

    }
    fun updatePiece(square:Pair<Int,Int>,piece:Piece) {
        rows[square.first][square.second] = piece
    }
    fun updateFromAnother(source:Board) {
        if(source == this) {
            return
        }
        for(i in 0..7)
        {

            for (j in 0..7) {
                rows[i][j] = source.rows[i][j]

            }
        }
    }
    override fun toString(): String {
        var res = ""
        for(i in rows) {
            for (j in i) {
                res += j.toString()
                res += " "
            }
            res += "\n"
        }
        return res
    }
}