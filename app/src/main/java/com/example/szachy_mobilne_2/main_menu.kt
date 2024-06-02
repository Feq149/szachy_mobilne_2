package com.example.szachy_mobilne_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.szachy_mobilne_2.database.DatabaseOfGames
 var database : DatabaseOfGames? = null
class main_menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseOfGames.getDatabase(this)
        setContentView(R.layout.activity_main_menu)
        configurePlayGameButton()
    }

    fun configurePlayGameButton() {
        val button = findViewById<Button>(R.id.game_button)
        button.setOnClickListener {
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}