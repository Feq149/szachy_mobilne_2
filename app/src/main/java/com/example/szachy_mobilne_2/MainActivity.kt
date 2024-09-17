package com.example.szachy_mobilne_2

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobilne_2.FullGameControl.GameBluetoothController
import com.example.szachy_mobilne_2.FullGameControl.GameController
import com.example.szachy_mobilne_2.View.ChessView


const val tag = "MAIN_CHESS_TAG"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        configureReturnToMainMenuButton()
        val gameConfiguration = gameSettings
        var gameController = GameController(findViewById<ChessView>(R.id.chess_view))
        if(!gameConfiguration.opponentName.equals("PC")) {
            gameController = GameBluetoothController(findViewById<ChessView>(R.id.chess_view),
                socket)
        }
        if(!gameConfiguration.color.equals("Random")) {
            gameController.userIsWhite = gameConfiguration.color.equals("White")
        }

        //showVictoryPopup()

        Log.d(tag, gameController.game.board.toString() + "\n")
        /*
                while(!gameController.game.isGameFinished) {
                    player1.playMove()

                    Log.d(tag, gameController.game.board.toString() + "\n")

                    player2.playMove()
                    Log.d(tag, gameController.game.board.toString() + "\n")

                }*/

    }
    fun configureReturnToMainMenuButton() {
        val button = findViewById<Button>(R.id.main_menu_button)
        button.setOnClickListener {
            finish()
        }
    }

    //broken, to be deleted probably

}