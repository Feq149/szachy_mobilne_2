package com.example.szachy_mobilne_2

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.szachy_mobilne_2.FullGameControl.GameReplayController
import com.example.szachy_mobilne_2.View.ChessView
import com.example.szachy_mobilne_2.View.ReplayDbGameView
import com.example.szachy_mobilne_2.database.GameDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DatabaseGameReplayActivity : AppCompatActivity() {
    var view : ReplayDbGameView? = null
    var gameDb : GameDb? = null
    var gameId : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_game_replay_view)
        configureGoBackButton()
        configureDeleteGameButton()
        configureNextButton()
        configurePrevButton()
       gameId = intent.getIntExtra("game_id",-1)

        if(gameId == -1) {
            Log.d(null,"id of replayed game was not provided")
            finish()
        }

        view = findViewById<ReplayDbGameView>(R.id.database_game_view)
        val games = database!!.dao.getGamesOrderedByDate()
        for (game in games) {
            if(game.id == gameId) {
                gameDb = game
                view!!.gameReplayController = GameReplayController(game)
            }
        }
        if(view!!.gameReplayController == null) {
            Log.d(null,"provided id of replayed game was not in database")
            finish()
        }
        view!!.invalidate()

    }

    fun configureDeleteGameButton() {
        val deleteButton = findViewById<Button>(R.id.delete_game_button)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    fun configureNextButton() {
        val button = findViewById<Button>(R.id.next_move_button)
        button.setOnClickListener {
            view?.gameReplayController?.turnToNextMoveAndGetThePosition()
            view?.invalidate()
        }
    }
    fun configurePrevButton() {
        val button = findViewById<Button>(R.id.previous_move_button)
        button.setOnClickListener {
            view?.gameReplayController?.turnToPreviousMoveAndGetThePosition()
            view?.invalidate()
        }
    }
    fun configureGoBackButton() {
        val button = findViewById<Button>(R.id.go_back_to_list_of_games_button)
        button.setOnClickListener {
            finish()
        }
    }

    private fun showDeleteConfirmationDialog() {
        // Inflate the custom layout
        val customLayout = layoutInflater.inflate(R.layout.delete_confirmaton_layout, null)

        // Create the AlertDialog
        val builder = AlertDialog.Builder(this, R.style.DeleteGameDialogue)
            .setView(customLayout)

        // Find and set up the buttons
        // Create and show the dialog
        val dialog = builder.create()
        dialog.show()
        val btnDelete: Button = customLayout.findViewById<Button>(R.id.btn_delete)
        val btnCancel: Button = customLayout.findViewById(R.id.btn_cancel)

        btnDelete.setOnClickListener {
            // Handle the delete action
            deleteGame()
            Toast.makeText(this, "Game deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()

            databaseGames?.remove(gameDb)
            adapter?.notifyDataSetChanged()

            finish()
        }

        btnCancel.setOnClickListener {
            // Dismiss the dialog
            dialog.dismiss()
        }


    }

    fun deleteGame() {
        MainScope().launch {
            database!!.dao.deleteGame(gameDb!!)
        }
    }
}