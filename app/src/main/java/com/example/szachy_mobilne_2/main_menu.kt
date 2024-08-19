package com.example.szachy_mobilne_2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobilne_2.database.DatabaseOfGames
import com.example.szachy_mobilne_2.database.GameDb


var database : DatabaseOfGames? = null
class main_menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseOfGames.getDatabase(this)
        setContentView(R.layout.activity_main_menu)
        configurePlayGameButton()
        configureDatabaseButton()
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
}