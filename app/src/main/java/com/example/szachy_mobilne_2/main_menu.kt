package com.example.szachy_mobilne_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobilne_2.database.DatabaseOfGames
import com.example.szachy_mobilne_2.database.GameDb


var database : DatabaseOfGames? = null
class main_menu : AppCompatActivity() {
    val opponents = mutableListOf("PC","Online")
    var selectedOpponent : String = "PC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseOfGames.getDatabase(this)
        setContentView(R.layout.activity_main_menu)
        configurePlayGameButton()
        configureDatabaseButton()
        configureGameChallengeButton()
        var list : List<GameDb>? = null

            list = database!!.dao.getGamesOrderedByDate()


        if(list == null ){
            Log.d(tag,"dupa")
        } else {
            for(a in list!!) {
                Log.d(tag,a.game)
            }
        }

    }
    fun configureDatabaseButton() {
        val button = findViewById<Button>(R.id.view_games_played_button)
        button.setOnClickListener {
            val intent = Intent(this,DatabaseActivity::class.java)
            startActivity(intent)
        }
    }

    fun configurePlayGameButton() {
        val button = findViewById<Button>(R.id.game_button)
        button.setOnClickListener {
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
            /*val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("You win!")
            alertDialogBuilder.setPositiveButton("ok") {_,_ ->


            }
            alertDialogBuilder.create().show()
           */
        }

    }

    fun configureGameChallengeButton() {
        val button = findViewById<Button>(R.id.challenge_settings)
        button.setOnClickListener {
            showOpponentColorDialog()
        }
    }

    private fun showOpponentColorDialog() {
        // Step 1: List of opponent names

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Choose Opponent and Color")

        // Step 2: Create a LinearLayout to hold the opponent spinner and color radio group
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Step 3: Create Spinner for opponent selection
        val spinner = Spinner(this)

        // Step 4: Create an ArrayAdapter for the Spinner using the opponent array
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opponents)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set default selected item (the first opponent)
        spinner.setSelection(0)

        // Add the Spinner to the layout
        layout.addView(spinner)

        // Step 5: Create a RadioGroup for color selection
        val radioGroup = RadioGroup(this)
        val radioBlack = RadioButton(this).apply { text = "Black" }
        val radioWhite = RadioButton(this).apply { text = "White" }
        val radioRandom = RadioButton(this).apply { text = "Random" }

        // Add radio buttons to RadioGroup
        radioGroup.addView(radioBlack)
        radioGroup.addView(radioWhite)
        radioGroup.addView(radioRandom)

        // Add RadioGroup to the layout
        layout.addView(radioGroup)

        // Step 6: Add OK and Cancel buttons
        builder.setView(layout)
        var selectedColor = "none"
        builder.setPositiveButton("OK") { dialog, _ ->
            // Get the selected opponent from the Spinner
            val selectedOpponent = spinner.selectedItem.toString()

            // Handle special actions: Add new opponent or Delete an opponent
            when (selectedOpponent) {
                "Online" -> {
                    showAddOpponentDialog(adapter)
                }
                else -> {
                    // Check which color is selected
                    selectedColor = when (radioGroup.checkedRadioButtonId) {
                        radioBlack.id -> "Black"
                        radioWhite.id -> "White"
                        radioRandom.id -> "Random"
                        else -> "No color selected"
                    }

                    // Handle the result
                    Toast.makeText(this, "Selected Opponent: $selectedOpponent\nSelected Color: $selectedColor", Toast.LENGTH_LONG).show()
                }
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        // Step 7: Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
    private fun showAddOpponentDialog(adapter: ArrayAdapter<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Name your opponent")

        // Add an EditText to allow the user to input the new opponent's name
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Set") { dialog, _ ->
            val chosenOpponent = input.text.toString()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }


    // Function to show a dialog to delete an existing opponent

}