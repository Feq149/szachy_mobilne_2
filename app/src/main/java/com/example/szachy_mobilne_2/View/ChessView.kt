package com.example.szachy_mobilne_2.View

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.szachy_mobile.Board
import com.example.szachy_mobile.EmptySquare
import com.example.szachy_mobile.Game
import com.example.szachy_mobilne_2.FullGameControl.GameController
import com.example.szachy_mobilne_2.R
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context,attrs) {
    var bok = 130f
    var odleglosc_od_lewej = 25f
    var odleglosc_od_gory = 500f
    val light_color = Paint()
    val dark_color = Paint()
    val pieceToBitmapConverter = mutableMapOf<Pieces,Bitmap>()
    val paint = Paint()
    var gameController = GameController(this)
    val modelToViewConverter = ModelToViewConverter()
    var prev_x_coordinate = -1
    var prev_y_coordinate = -1
    var enableMove = true
    var moving_piece_x_coordinate = -1f
    var moving_piece_y_coordinate = -1f
    val colors = arrayOf(light_color,dark_color)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        prepareScale(canvas)
        prepareBitmaps()
        light_color.color = Color.LTGRAY
        dark_color.color = Color.CYAN
        drawBoard(canvas)
        drawPiecesAtBoard(canvas,gameController.game.board)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(smaller,smaller)
    }
    fun prepareScale(canvas : Canvas) {
        val scale = 0.9f
        val boardSideLength = min(canvas.width,canvas.height) * scale
        bok = boardSideLength / 8f
        odleglosc_od_lewej = (canvas.width - boardSideLength) / 2f
        odleglosc_od_gory = (canvas.height - boardSideLength) / 2f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) {return false;}
        when (event.action) {
            MotionEvent.ACTION_DOWN->{
                if(!enableMove) {
                    Log.d(com.example.szachy_mobilne_2.tag,"trying to move on not player's turn")
                    return true
                }
                val cords = getSquareFromCoordinates(event.y,event.x)
                Log.d(com.example.szachy_mobilne_2.tag,"down at ${event.y}, ${event.x}, after conversion square is ${cords.first}, ${cords.second}")
                prev_x_coordinate = cords.first
                prev_y_coordinate = cords.second
                if(gameController.game.board.rows[cords.first][cords.second] !is EmptySquare) {
                    moving_piece_x_coordinate = event.x
                    moving_piece_y_coordinate = event.y
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(!enableMove) {return true}
                if(gameController.game.isGameFinished){
                    return true
                }
                moving_piece_x_coordinate = event.x
                moving_piece_y_coordinate = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP->{
                if(!enableMove) {return true}
                val cords = getSquareFromCoordinates(event.y,event.x)
                Log.d(com.example.szachy_mobilne_2.tag,"up at ${event.y}, ${event.x}, after conversion square is ${cords.first}, ${cords.second}")
                val move = Pair(Pair(prev_x_coordinate,prev_y_coordinate),cords)
                prev_x_coordinate = -1
                prev_y_coordinate = -1
                val isMoveSuccessfull = gameController.makeAMove(move)
                invalidate()
                if(isMoveSuccessfull) {
                    enableMove = false
                    gameController.getOpponentMovePlayed()
                }
            }




        }


        return true;
    }
    fun getSquareFromCoordinates(pionowa: Float, pozioma: Float) :Pair<Int,Int> {
        val resX = ((pionowa - odleglosc_od_gory)/bok).toInt()
        val resY = ((pozioma - odleglosc_od_lewej)/bok).toInt()
        return Pair(resX,resY)
    }
    fun drawBoard(canvas:Canvas) {


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

    fun drawPiecesForCorrectPerspective(canvas:Canvas,board:Board) {

    }
    fun drawPiecesAtBoard(canvas:Canvas,board:Board) {
        for (i in 0..7) {
            for (j in 0..7) {
                val piece = modelToViewConverter.getViewPieceFromModelPiece(board.rows[i][j]) ?: continue
                if(i == prev_x_coordinate && j == prev_y_coordinate) {
                    canvas.drawBitmap(pieceToBitmapConverter[piece]!! ,null, RectF(moving_piece_x_coordinate - bok/2,moving_piece_y_coordinate- bok/2,moving_piece_x_coordinate + bok/2,moving_piece_y_coordinate + bok/2),paint)
                    continue
                }
                drawPieceAtSquare(canvas, i, j, piece)


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