package com.example.szachy_mobilne_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobile.Player
import com.example.szachy_mobilne_2.FullGameControl.GameController
import com.example.szachy_mobilne_2.View.ChessView


const val tag = "MAIN_CHESS_TAG"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        configureReturnToMainMenuButton()
        val gameController = GameController(findViewById<ChessView>(R.id.chess_view))
        gameController.chessView.invalidate()

        val player1 = Player(gameController, true)
        val player2 = Player(gameController, false)

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
}