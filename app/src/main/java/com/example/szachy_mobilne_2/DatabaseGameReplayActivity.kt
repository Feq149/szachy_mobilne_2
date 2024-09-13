package com.example.szachy_mobilne_2

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobilne_2.View.ChessView
import com.example.szachy_mobilne_2.View.ReplayDbGameView

class DatabaseGameReplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.db_game_replay_view)
        configureGoBackButtonButton()
        configureDeleteGameButton()
        val view = findViewById<ReplayDbGameView>(R.id.database_game_view)
        view.invalidate()
    }

    fun configureDeleteGameButton() {
        val deleteButton = findViewById<Button>(R.id.delete_game_button)
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    fun configureGoBackButtonButton() {
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
            deleteGame()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            // Dismiss the dialog
            dialog.dismiss()
        }


    }

    fun deleteGame() {

    }
}