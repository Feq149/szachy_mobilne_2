package com.example.szachy_mobilne_2.View

import com.example.szachy_mobile.Bishop
import com.example.szachy_mobile.King
import com.example.szachy_mobile.Knight
import com.example.szachy_mobile.Pawn
import com.example.szachy_mobile.Piece
import com.example.szachy_mobile.Queen
import com.example.szachy_mobile.Rook

class ModelToViewConverter {

    fun getViewPieceFromModelPiece(piece : Piece) : Pieces? {
        if(piece is Queen) {
            if (piece.isWhite) {
                return Pieces.white_queen
            }
            return Pieces.black_queen
        }
        if(piece is Rook) {
            if(piece.isWhite) {
                return Pieces.white_rook
            }
            return Pieces.black_rook
        }
        if(piece is Bishop) {
            if (piece.isWhite) {
                return Pieces.white_bishop
            }
            return Pieces.black_biskop
        }
        if(piece is Knight) {
            if (piece.isWhite) {
                return Pieces.white_knight
            }
            return Pieces.black_knight
        }
        if(piece is Pawn) {
            if (piece.isWhite) {
                return Pieces.white_pawn
            }
            return Pieces.black_pawn
        }
        if(piece is King) {
            if (piece.isWhite) {
                return Pieces.white_king
            }
            return Pieces.black_king
        }
        return null
    }
}