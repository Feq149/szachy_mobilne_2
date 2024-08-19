package com.example.szachy_mobilne_2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.szachy_mobilne_2.View.DatabaseView

class DatabaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.database_scroll_view)
        val view = findViewById<DatabaseView>(R.id.database_view)
        view.invalidate()
    }
}