package com.example.szachy_mobilne_2.View

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.szachy_mobile.Board
import com.example.szachy_mobilne_2.R

class ChessView(context: Context?, attrs: AttributeSet?) : View(context,attrs) {
    private val bok = 130f
    val odleglosc_od_lewej = 25f
    val odleglosc_od_gory = 500f
    val light_color = Paint()
    val dark_color = Paint()
    val pieceToBitmapConverter = mutableMapOf<Pieces,Bitmap>()
    val paint = Paint()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        prepareBitmaps()
        light_color.color = Color.LTGRAY
        dark_color.color = Color.CYAN
        drawBoard(canvas)
        drawStartingPosition(canvas)
    }
    fun drawBoard(canvas:Canvas) {

        val colors = arrayOf(light_color,dark_color)
        for(j in 0..7) {
            for (i in 0..7) {
                canvas.drawRect(
                    odleglosc_od_lewej + i * bok,
                    odleglosc_od_gory + j * bok ,
                    odleglosc_od_lewej + (i + 1) * bok,
                    odleglosc_od_gory + (j + 1)*bok,
                    colors[(i + j) % 2]
                )

            }
        }
    }
    fun prepareBitmaps() {
        pieceToBitmapConverter[Pieces.white_bishop] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_blt60
        )
        pieceToBitmapConverter[Pieces.black_biskop] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_bdt60
        )
        pieceToBitmapConverter[Pieces.white_pawn] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_plt60
        )
        pieceToBitmapConverter[Pieces.black_pawn] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_pdt60
        )
        pieceToBitmapConverter[Pieces.white_rook] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_rlt60
        )
        pieceToBitmapConverter[Pieces.black_rook] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_rdt60
        )
        pieceToBitmapConverter[Pieces.white_knight] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_nlt60
        )
        pieceToBitmapConverter[Pieces.black_knight] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_ndt60
        )
        pieceToBitmapConverter[Pieces.white_queen] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_qlt60
        )
        pieceToBitmapConverter[Pieces.black_queen] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_qdt60
        )
        pieceToBitmapConverter[Pieces.white_king] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_klt60
        )
        pieceToBitmapConverter[Pieces.black_king] = BitmapFactory.decodeResource(resources,
            R.drawable.chess_kdt60
        )
    }
    fun drawPieceAtSquare(canvas:Canvas,x:Int,y:Int, piece: Pieces) {
        canvas.drawBitmap(pieceToBitmapConverter[piece]!! ,null, RectF(odleglosc_od_lewej + y * bok,odleglosc_od_gory + x * bok,odleglosc_od_lewej + (y + 1)*bok,odleglosc_od_gory + (x + 1)*bok),paint)
    }
    fun drawPiecesAtBoard(canvas:Canvas,board:Board) {
        for (i in 0..7) {
            for (j in 0..7) {

            }
        }
    }

    fun drawStartingPosition(canvas: Canvas) {
        for(i in 0..7) {
            var pieceToDraw : Pieces
            if(i == 0 || i == 7) {
                pieceToDraw = Pieces.black_rook
            } else if(i == 1 || i == 6) {
                pieceToDraw = Pieces.black_knight
            } else if(i == 2 || i == 5) {
                pieceToDraw = Pieces.black_biskop
            } else if(i == 3) {
                pieceToDraw = Pieces.black_queen
            } else {
                pieceToDraw = Pieces.black_king
            }
            drawPieceAtSquare(canvas,0,i,pieceToDraw)
        }
        for(i in 0..7) {
            var pieceToDraw : Pieces
            if(i == 0 || i == 7) {
                pieceToDraw = Pieces.white_rook
            } else if(i == 1 || i == 6) {
                pieceToDraw = Pieces.white_knight
            } else if(i == 2 || i == 5) {
                pieceToDraw = Pieces.white_bishop
            } else if(i == 3) {
                pieceToDraw = Pieces.white_queen
            } else {
                pieceToDraw = Pieces.white_king
            }
            drawPieceAtSquare(canvas,7,i,pieceToDraw)
        }
        for(i in 0..7) {
            drawPieceAtSquare(canvas,1,i, Pieces.black_pawn)
            drawPieceAtSquare(canvas,6,i, Pieces.white_pawn)
        }

    }

}