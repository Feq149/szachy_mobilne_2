package com.example.szachy_mobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

import com.example.szachy_mobilne_2.R

private const val tag = "MAIN_CHESS_TAG"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val p = Game()
        val player1 = Player(p,true)
        val player2 = Player(p,false)

        Log.d(tag,p.board.toString() + "\n")

        while(!p.isGameFinished) {
            player1.playMove()
            Log.d(tag,p.board.toString() + "\n")
            player2.playMove()
            Log.d(tag,p.board.toString() + "\n")
        }

    }
}

