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
import com.example.szachy_mobilne_2.FullGameControl.GameReplayController
import com.example.szachy_mobilne_2.R
import kotlin.math.min

class ReplayDbGameView (context: Context?, attrs: AttributeSet?) : View(context,attrs) {
    private var boardHeight = 0f
    var bok = 130f
    var odleglosc_od_lewej = 25f
    var odleglosc_od_gory = 500f
    val light_color = Paint()
    val dark_color = Paint()
    val pieceToBitmapConverter = mutableMapOf<Pieces, Bitmap>()
    val paint = Paint()
    var gameReplayController : GameReplayController? = null
    val modelToViewConverter = ModelToViewConverter()
    val colors = arrayOf(light_color,dark_color)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        prepareScale(canvas)
        prepareBitmaps()
        light_color.color = Color.LTGRAY
        dark_color.color = Color.CYAN
        drawBoard(canvas)
        drawPiecesAtBoard(canvas,gameReplayController!!.getCurrentPosition())
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
    fun prepareScale(canvas : Canvas) {
        val scale = 0.9f
        val boardSideLength = min(canvas.width,canvas.height) * scale
        bok = boardSideLength / 8f
        odleglosc_od_lewej = (canvas.width - boardSideLength) / 2f
        odleglosc_od_gory = (canvas.height - boardSideLength) / 2f
        boardHeight = boardSideLength
    }



    fun drawBoard(canvas: Canvas) {
        if(true) {
            drawBoardIfUserWhite(canvas)
        }
        else {
            drawBoardIfUserBlack(canvas)
        }

    }
    fun drawBoardIfUserWhite(canvas: Canvas) {
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
    fun drawBoardIfUserBlack(canvas: Canvas) {
        for(j in 0..7) {
            for (i in 0..7) {
                canvas.drawRect(
                    odleglosc_od_lewej + i * bok,
                    odleglosc_od_gory + j * bok ,
                    odleglosc_od_lewej + (i + 1) * bok,
                    odleglosc_od_gory + (j + 1)*bok,
                    colors[(i + j + 1) % 2]
                )

            }
        }
    }
    fun drawPieceAtSquare(canvas:Canvas,x:Int,y:Int, piece: Pieces) {
        canvas.drawBitmap(pieceToBitmapConverter[piece]!! ,null, RectF(odleglosc_od_lewej + y * bok,odleglosc_od_gory + x * bok,odleglosc_od_lewej + (y + 1)*bok,odleglosc_od_gory + (x + 1)*bok),paint)
    }

    fun drawPiecesAtBoard(canvas:Canvas,board:Board) {
        if(gameReplayController!!.userIsWhite) {
            drawPiecesAtBoardIfUserWhite(canvas,board)
        } else {
            drawPiecesAtBoardIfUserBlack(canvas,board)
        }

    }
    fun drawPiecesAtBoardIfUserWhite(canvas:Canvas,board:Board) {
        for (i in 0..7) {
            for (j in 0..7) {
                val piece = modelToViewConverter.getViewPieceFromModelPiece(board.rows[i][j]) ?: continue
                drawPieceAtSquare(canvas, i, j, piece)


            }
        }
    }
    fun drawPiecesAtBoardIfUserBlack(canvas:Canvas,board:Board) {
        for (i in 0..7) {
            for (j in 0..7) {
                val piece = modelToViewConverter.getViewPieceFromModelPiece(board.rows[7 - i][7 - j]) ?: continue

                drawPieceAtSquare(canvas, i, j, piece)


            }
        }
    }


}