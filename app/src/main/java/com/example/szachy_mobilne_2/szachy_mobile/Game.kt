package com.example.szachy_mobile

class Game {
    var whiteToMove = true
    var board = Board()
    var isWhiteLongCastlesLegal = true
    var isWhiteShortCastlesLegal = true
    var isBlackLongCastlesLegal = true
    var isBlackShortCastlesLegal = true
    var movesWithoutPawnOrCapture = 0
    var isGameFinished = false
    fun isDraw() :Boolean{
        if(!isGameFinished) {return false}
        if(!isKingAttacked(board,whiteToMove)) {
            return true
        }
        return false
    }
    fun didWhiteWin():Boolean {
        if(!isGameFinished) {return false}
        if(isDraw()) {return false}
        if(whiteToMove) {
            return false
        }
        return true
    }
    fun isDrawByInsufficientMaterial():Boolean{
        var knightCount = 0
        var bishopCount = 0
        for(i in 0..7) {
            for (j in 0..7) {
                if(board.rows[i][j] is Queen || board.rows[i][j] is Rook || board.rows[i][j] is Pawn) {
                    return false
                }
                if(board.rows[i][j] is Knight) {
                    knightCount++
                } else if(board.rows[i][j] is Bishop) {
                    bishopCount++
                }
                if(bishopCount > 1 || knightCount > 1) {
                    return false
                }
                if(bishopCount == 1 && knightCount == 1) {
                    return false
                }
            }
        }
        return true
    }
    fun makeAMove(move:Pair<Pair<Int,Int>,Pair<Int,Int>>):Boolean{
        if(isGameFinished) {return false}
        if(move.first == move.second) {
            return false
        }
        val legalMoves = getLegalMoves(whiteToMove)
        if(legalMoves.isEmpty()) {
            isGameFinished = true
            return false
        }
        if(legalMoves.contains(move)) {
            val isCapture = board.rows[move.second.first][move.second.second] !is EmptySquare
            val isPawn = board.rows[move.first.first][move.first.second] is Pawn
            val originalPiece = board.rows[move.first.first][move.first.second]
            if(originalPiece is King) {
                if(originalPiece.isWhite) {
                    isWhiteLongCastlesLegal = false
                    isWhiteShortCastlesLegal = false
                } else {
                    isBlackShortCastlesLegal = false
                    isBlackLongCastlesLegal = false
                }
            }
            if(originalPiece is Rook) {
                if(move.first.first == 0 && move.first.second == 0) {
                    isBlackLongCastlesLegal = false
                }
                if(move.first.first == 0 && move.first.second == 7) {
                    isBlackShortCastlesLegal = false
                }
                if(move.first.first == 7 && move.first.second == 0) {
                    isWhiteLongCastlesLegal = false
                }
                if(move.first.first == 7 && move.first.second == 7) {
                    isWhiteShortCastlesLegal = false
                }
            }
            if(move.second.first == 0 && move.second.second == 0) {
                isBlackLongCastlesLegal = false
            }
            if(move.second.first == 0 && move.second.second == 7) {
                isBlackShortCastlesLegal = false
            }
            if(move.second.first == 7 && move.second.second == 0) {
                isWhiteLongCastlesLegal = false
            }
            if(move.second.first == 7 && move.second.second == 7) {
                isWhiteShortCastlesLegal = false
            }
            if(originalPiece.isWhite && originalPiece is King) {
                if(move.first == Pair(7,4) && move.second == Pair(7,6)) {
                    board.movePiece(Pair(7,7), Pair(7,5))
                }
                if(move.first == Pair(7,4) && move.second == Pair(7,2)) {
                    board.movePiece(Pair(7,0), Pair(7,3))
                }

            }
            if(!originalPiece.isWhite && originalPiece is King) {
                if(move.first == Pair(0,4) && move.second == Pair(0,6)) {
                    board.movePiece(Pair(0,7), Pair(0,5))
                }
                if(move.first == Pair(0,4) && move.second == Pair(0,2)) {
                    board.movePiece(Pair(0,0), Pair(0,3))
                }
            }
            board.movePiece(move.first,move.second)

            if(move.second.first == 0 || move.second.first == 7) {
                if(board.rows[move.second.first][move.second.second] is Pawn) {
                    val promotion = Queen("queen",board.rows[move.second.first][move.second.second].isWhite,board)
                    board.updatePiece(move.second,promotion)
                }
            }
            whiteToMove = !whiteToMove
            if(isCapture || isPawn){
                movesWithoutPawnOrCapture = 0
            } else {
                movesWithoutPawnOrCapture++
                if(movesWithoutPawnOrCapture >= 50) {
                    isGameFinished = true
                }
            }
            if(isDrawByInsufficientMaterial()) {
                isGameFinished = true
            }
            if(getLegalMoves(whiteToMove).isEmpty()) {
                isGameFinished = true
            }
            return true
        } else {
            return false
        }
    }
    fun isKingAttacked(board: Board,whiteKing:Boolean): Boolean{
        for (i in 0..7) {
            for (j in 0..7) {
                val currentPiece = board.rows[i][j]
                if(currentPiece is EmptySquare || currentPiece.isWhite != whiteKing) {continue}
                if(currentPiece is King) {
                    var k = i - 1
                    var l = j
                    if(k >= 0 &&board.rows[k][l] is King) {return true}
                    while(k >= 0) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Rook)) {
                                return true
                            }
                            break
                        }
                        k--
                    }
                    k = i + 1

                    l = j
                    if(k != 8 && board.rows[k][l] is King) {return true}
                    while(k <= 7) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Rook)) {
                                return true
                            }
                            break
                        }
                        k++
                    }

                    k = i
                    l = j - 1
                    if(l >= 0 && board.rows[k][l] is King) {return true}
                    while(l >= 0) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Rook)) {
                                return true
                            }
                            break
                        }
                        l--
                    }
                    k = i
                    l = j + 1
                    if(l != 8 && board.rows[k][l] is King) {return true}
                    while(l <= 7) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Rook)) {
                                return true
                            }
                            break
                        }
                        l++
                    }

                    k = i - 1
                    l = j - 1
                    if(k >= 0 && l >= 0 && board.rows[k][l] is King) {return true}
                    while(k >= 0 && l >= 0) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Bishop)) {
                                return true
                            }
                            break
                        }
                        k--
                        l--
                    }
                    k = i - 1
                    l = j + 1
                    if(k >= 0 && l <= 7 && board.rows[k][l] is King) {return true}
                    while(k >= 0 && l <= 7) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Bishop)) {
                                return true
                            }
                            break
                        }
                        k--
                        l++
                    }
                    k = i + 1
                    l = j - 1
                    if(k <= 7 && l >= 0 && board.rows[k][l] is King) {return true}
                    while(k <= 7 && l >= 0) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Bishop)) {
                                return true
                            }
                            break
                        }
                        k++
                        l--
                    }
                    k = i + 1
                    l = j + 1
                    if(k <= 7 && l <= 7 && board.rows[k][l] is King) {return true}
                    while(k < 8 && l < 8) {
                        if(board.rows[k][l] !is EmptySquare) {
                            if(  board.rows[k][l].isWhite != currentPiece.isWhite && (board.rows[k][l] is Queen || board.rows[k][l] is Bishop)) {
                                return true
                            }
                            break
                        }
                        k++
                        l++
                    }

                    if(i > 1 && j > 0 && board.rows[i-2][j-1] is Knight && board.rows[i - 2][j - 1].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i > 0 && j > 1 && board.rows[i-1][j-2] is Knight && board.rows[i - 1][j - 2].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i > 1 && j < 7 && board.rows[i-2][j+1] is Knight && board.rows[i - 2][j + 1].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i > 0 && j < 6 && board.rows[i-1][j+2] is Knight && board.rows[i - 1][j + 2].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i < 6 && j > 0 && board.rows[i+2][j-1] is Knight && board.rows[i + 2][j - 1].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i < 7 && j > 1 && board.rows[i+1][j-2] is Knight && board.rows[i + 1][j - 2].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i < 7 && j < 6 && board.rows[i+1][j+2] is Knight && board.rows[i + 1][j + 2].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(i < 6 && j < 7 && board.rows[i+2][j+1] is Knight && board.rows[i + 2][j + 1].isWhite != currentPiece.isWhite) {
                        return true
                    }
                    if(currentPiece.isWhite) {
                        if(i > 0 && j > 0) {
                            if(!board.rows[i - 1][j - 1].isWhite && board.rows[i-1][j-1] is Pawn) {
                                return true
                            }
                        }
                        if( i > 0 && j < 7) {
                            if(!board.rows[i - 1][j + 1].isWhite && board.rows[i-1][j+1] is Pawn) {
                                return true
                            }
                        }
                    } else {
                        if(i < 7 && j > 0) {
                            if(board.rows[i + 1][j - 1].isWhite && board.rows[i+1][j-1] is Pawn) {
                                return true
                            }
                        }
                        if( i < 7 && j < 7) {
                            if(board.rows[i + 1][j + 1].isWhite && board.rows[i+1][j+1] is Pawn) {
                                return true
                            }
                        }
                    }
                    return false
                }
            }
        }
        return true
    }
    fun getLegalMoves (forWhite:Boolean):MutableList<Pair<Pair<Int,Int>,Pair<Int,Int>>> {
        val res = mutableListOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()
        val tempBoard = Board()
        for (i in 0..7) {
            for (j in 0..7) {

                val currentPiece = board.rows[i][j]
                if(currentPiece is EmptySquare ) {continue}
                if(currentPiece.isWhite != forWhite) {continue}
                if(currentPiece is King) {
                    if(i > 0) {

                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i-1,j))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i - 1, j)))
                        }
                        if(j > 0) {
                            tempBoard.updateFromAnother(board)
                            tempBoard.movePiece(Pair(i,j),Pair(i-1,j - 1))
                            if(!isKingAttacked(tempBoard,forWhite)) {
                                res.add(Pair(Pair(i,j),Pair(i - 1, j - 1)))
                            }
                        }
                        if(j < 7) {
                            tempBoard.updateFromAnother(board)
                            tempBoard.movePiece(Pair(i,j),Pair(i-1,j + 1))
                            if(!isKingAttacked(tempBoard,forWhite)) {
                                res.add(Pair(Pair(i,j),Pair(i - 1, j + 1)))
                            }
                        }
                    }
                    if(i < 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i+1,j))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i + 1,j)))
                        }
                        if(j > 0) {
                            tempBoard.updateFromAnother(board)
                            tempBoard.movePiece(Pair(i,j),Pair(i+1,j - 1))
                            if(!isKingAttacked(tempBoard,forWhite)) {
                                res.add(Pair(Pair(i,j),Pair(i + 1, j - 1)))
                            }
                        }
                        if(j < 7) {
                            tempBoard.updateFromAnother(board)
                            tempBoard.movePiece(Pair(i,j),Pair(i+1,j + 1))
                            if(!isKingAttacked(tempBoard,forWhite)) {
                                res.add(Pair(Pair(i,j),Pair(i + 1, j + 1)))
                            }
                        }
                    }
                    if(j > 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i,j - 1))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i, j - 1)))
                        }
                    }
                    if(j < 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i,j + 1))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i, j + 1)))
                        }
                    }
                }
                else if(currentPiece is Knight) {
                    if(i > 1 && j > 0 ) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i - 2,j - 1))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i - 2, j - 1)))
                        }
                    }
                    if(i > 0 && j > 1 ) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i - 1,j - 2))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i - 1, j  - 2)))
                        }
                    }
                    if(i > 1 && j < 7 ) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i - 2,j + 1))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i - 2, j + 1)))
                        }
                    }
                    if(i > 0 && j < 6 ) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i - 1,j + 2))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i - 1, j + 2)))
                        }
                    }
                    if(i < 6 && j > 0 ) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i + 2,j - 1))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i + 2, j - 1)))
                        }
                    }
                    if(i < 7 && j > 1) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i + 1,j - 2))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i + 1, j -2)))
                        }
                    }
                    if(i < 7 && j < 6 ) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i + 1,j + 2))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i + 1, j + 2)))
                        }
                    }
                    if(i < 6 && j < 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(i + 2,j + 1))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(i + 2, j + 1)))
                        }
                    }

                } else if( currentPiece is Pawn) {
                    if(currentPiece.isWhite) {

                        if(i == 6) {
                            if(board.rows[5][j] is EmptySquare && board.rows[4][j] is EmptySquare) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i - 2,j))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i - 2, j)))
                                }
                            }
                        }
                        if(i > 0) {
                            if(board.rows[i - 1][j] is EmptySquare) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i - 1,j))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i - 1, j)))
                                }
                            }

                            if(j > 0 && board.rows[i-1][j-1] !is EmptySquare && board.rows[i-1][j-1].isWhite != currentPiece.isWhite) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i - 1,j - 1))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i - 1, j - 1)))
                                }
                            }

                            if(j < 7 && board.rows[i-1][j+1] !is EmptySquare && board.rows[i-1][j+1].isWhite != currentPiece.isWhite) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i - 1,j + 1))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i - 1, j + 1)))
                                }
                            }
                        }
                    } else {
                        if(i == 1) {
                            if(board.rows[2][j] is EmptySquare && board.rows[3][j] is EmptySquare) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i + 2,j))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i + 2, j)))
                                }
                            }
                        }
                        if(i < 7) {
                            if(board.rows[i + 1][j] is EmptySquare) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i + 1,j))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i + 1, j)))
                                }
                            }

                            if(j > 0 && board.rows[i+1][j-1] !is EmptySquare && board.rows[i+1][j-1].isWhite != currentPiece.isWhite) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i + 1,j - 1))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i + 1, j - 1)))
                                }
                            }

                            if(j < 7 && board.rows[i+1][j+1] !is EmptySquare && board.rows[i+1][j+1].isWhite != currentPiece.isWhite) {
                                tempBoard.updateFromAnother(board)
                                tempBoard.movePiece(Pair(i,j),Pair(i + 1,j + 1))
                                if(!isKingAttacked(tempBoard,forWhite)) {
                                    res.add(Pair(Pair(i,j),Pair(i + 1, j + 1)))
                                }
                            }
                        }
                    }
                } else if(currentPiece is Bishop) {
                    var k = i - 1
                    var l = j - 1
                    while(k >= 0 && l >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k--
                        l--
                    }
                    k = i + 1
                    l = j - 1
                    while(k <= 7 && l >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k++
                        l--
                    }
                    k = i - 1
                    l = j + 1
                    while(k >=0 && l <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k--
                        l++
                    }
                    k = i + 1
                    l = j + 1
                    while(k <= 7 && l <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k++
                        l++
                    }
                } else if(currentPiece is Rook) {
                    var k = i - 1
                    var l = j
                    while(k >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k--
                    }
                    k = i + 1
                    l = j
                    while(k <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k++
                    }
                    k = i
                    l = j + 1
                    while(l <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        l++
                    }
                    k = i
                    l = j - 1
                    while(l >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        l--
                    }

                } else if(currentPiece is Queen) {
                    var k = i - 1
                    var l = j
                    while(k >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k--
                    }
                    k = i + 1
                    l = j
                    while(k <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k++
                    }
                    k = i
                    l = j + 1
                    while(l <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        l++
                    }
                    k = i
                    l = j - 1
                    while(l >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        l--
                    }

                    k = i - 1
                    l = j - 1
                    while(k >= 0 && l >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k--
                        l--
                    }
                    k = i + 1
                    l = j - 1
                    while(k <= 7 && l >= 0) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k++
                        l--
                    }
                    k = i - 1
                    l = j + 1
                    while(k >=0 && l <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k--
                        l++
                    }
                    k = i + 1
                    l = j + 1
                    while(k <= 7 && l <= 7) {
                        tempBoard.updateFromAnother(board)
                        tempBoard.movePiece(Pair(i,j),Pair(k,l))
                        if(!isKingAttacked(tempBoard,forWhite)) {
                            res.add(Pair(Pair(i,j),Pair(k, l)))
                        }
                        if(board.rows[k][l] !is EmptySquare) {
                            break
                        }
                        k++
                        l++
                    }
                }
            }
        }
        val filtered = mutableListOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()
        for (move in res) {
            if(board.rows[move.second.first][move.second.second].isWhite != forWhite || board.rows[move.second.first][move.second.second] is EmptySquare) {
                filtered.add(move)
            }
        }

        if(forWhite) {
            if(isWhiteShortCastlesLegal) {
                if(board.rows[7][5] is EmptySquare && board.rows[7][6] is EmptySquare) {
                    var canWhiteCastleShort = true
                    tempBoard.updateFromAnother(board)
                    tempBoard.movePiece(Pair(7,4),Pair(7,5))
                    canWhiteCastleShort = canWhiteCastleShort && !isKingAttacked(tempBoard,forWhite)
                    tempBoard.movePiece(Pair(7,5),Pair(7,6))
                    canWhiteCastleShort = canWhiteCastleShort && !isKingAttacked(tempBoard,forWhite)
                    if(canWhiteCastleShort) {
                        filtered.add(Pair(Pair(7,4),Pair(7,6)))
                    }
                }
            }
        }

        if(forWhite) {
            if(isWhiteLongCastlesLegal) {
                if(board.rows[7][3] is EmptySquare && board.rows[7][2] is EmptySquare&& board.rows[7][1] is EmptySquare) {
                    var canWhiteCastleLong = true
                    tempBoard.updateFromAnother(board)
                    tempBoard.movePiece(Pair(7,4),Pair(7,3))
                    canWhiteCastleLong = canWhiteCastleLong && !isKingAttacked(tempBoard,forWhite)
                    tempBoard.movePiece(Pair(7,5),Pair(7,2))
                    canWhiteCastleLong = canWhiteCastleLong && !isKingAttacked(tempBoard,forWhite)

                    if(canWhiteCastleLong) {
                        filtered.add(Pair(Pair(7,4),Pair(7,2)))
                    }
                }
            }
        }



        if(!forWhite) {
            if(isBlackShortCastlesLegal) {
                if(board.rows[0][5] is EmptySquare && board.rows[0][6] is EmptySquare) {
                   var  canBlackCastleShort = true
                    tempBoard.updateFromAnother(board)
                    tempBoard.movePiece(Pair(0,4),Pair(0,5))
                    canBlackCastleShort = canBlackCastleShort && !isKingAttacked(tempBoard,!forWhite)
                    tempBoard.movePiece(Pair(0,5),Pair(0,6))
                    canBlackCastleShort = canBlackCastleShort && !isKingAttacked(tempBoard,!forWhite)
                    if(canBlackCastleShort) {
                        filtered.add(Pair(Pair(0,4),Pair(0,6)))
                    }
                }
            }
        }

        if(!forWhite) {
            if(isBlackLongCastlesLegal) {
                if(board.rows[0][3] is EmptySquare && board.rows[0][2] is EmptySquare&& board.rows[0][1] is EmptySquare) {
                    var canBlackCastleLong = true
                    tempBoard.updateFromAnother(board)
                    tempBoard.movePiece(Pair(0,4),Pair(0,3))
                    canBlackCastleLong = canBlackCastleLong && !isKingAttacked(tempBoard,!forWhite)
                    tempBoard.movePiece(Pair(0,5),Pair(0,2))
                    canBlackCastleLong = canBlackCastleLong && !isKingAttacked(tempBoard,!forWhite)


                    if(canBlackCastleLong) {
                        filtered.add(Pair(Pair(0,4),Pair(0,2)))
                    }
                }
            }
        }

        return filtered
    }


    override fun toString(): String {
        return "responsible for chess game"

    }
}