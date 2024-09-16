package com.example.szachy_mobilne_2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobile.Game
import com.example.szachy_mobilne_2.View.DatabaseView
import com.example.szachy_mobilne_2.database.GameDb

var databaseGames : MutableList<GameDb>? = null
var adapter : BaseAdapter? = null
class DatabaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.database_scroll_view)
        //val view = findViewById<DatabaseView>(R.id.database_view)
        //view.invalidate()
        setContentView(R.layout.database_listview)
        val databaseListView : ListView = findViewById(R.id.database_listview_id)
        databaseGames = database!!.dao.getGamesOrderedByDate().toMutableList();
        databaseListView.setOnItemClickListener { parent, view, position, id ->
            val selectedGame = databaseGames!![position]

            val intent = Intent(this, DatabaseGameReplayActivity::class.java)
            intent.putExtra("game_id", selectedGame.id)
            // Pass the game object to the new activity
            //intent.putExtra("selected_game", selectedGame)

            startActivity(intent)
        }
        adapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,
            databaseGames!!
        )
        databaseListView.adapter = adapter
        configureMainMenuButton()

    }
    fun configureMainMenuButton() {
        val button = findViewById<Button>(R.id.main_menu_button_db)
        button.setOnClickListener {
            finish()
        }

    }
}