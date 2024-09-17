package com.example.szachy_mobilne_2

import android.content.Intent
import android.os.Build
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
import androidx.core.app.ActivityCompat
import com.example.szachy_mobilne_2.FullGameControl.GameSettings
import com.example.szachy_mobilne_2.database.DatabaseOfGames
import com.example.szachy_mobilne_2.database.GameDb
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Parcel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.UUID
import kotlin.concurrent.thread

var database : DatabaseOfGames? = null
var gameSettings = GameSettings("PC","Random")
var socket: BluetoothSocket? = null
class main_menu<BluetoothServerSocket> : AppCompatActivity(),IncomingGameListener {
    val opponents = mutableListOf("PC","Online")
    var opponentName = "PC"
    var playerColor = "Random"
    private val appName = "Mobilne_Szachy"
    private val uuid: UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    var serverSocket: android.bluetooth.BluetoothServerSocket? = null
    val eventListener : IncomingGameListener = this
    private fun enableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
        } else {
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                checkBluetoothPermissions()
                try {
                    startActivityForResult(enableBtIntent, 1)
                } catch(e : SecurityException) {
                    Log.d("","permissions are extremally screwed")
                    return
                }

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseOfGames.getDatabase(this)
        setContentView(R.layout.activity_main_menu)
        configurePlayGameButton()
        configureDatabaseButton()
        configureGameChallengeButton()
        try {


            serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(appName, uuid)
        }
        catch (e : SecurityException) {
            Log.d("","permissions are screwed")
            return
        }
        startAwaitingForUpcomingChallenge()



    }

    override fun onEventTriggered(event: IncomingGameEvent) {
        manageConnectedSocket(socket!!)
        //Toast.makeText(this,"socket accept successful",Toast.LENGTH_LONG).show()
    // val i = 4;
    }

    private fun startAwaitingForUpcomingChallenge() {
        thread {
            var shouldLoop = true

            while(shouldLoop) {

                socket = try {
                    serverSocket?.accept()
                } catch (e: IOException) {
                    Log.e("second thread", "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    //manageMyConnectedSocket(it)
                    //mmServerSocket?.close()
                    eventListener.onEventTriggered(IncomingGameEvent())
                    shouldLoop = false
                }
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
    private val bluetoothPermissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,

    )

    private fun checkBluetoothPermissions() {
        if (bluetoothPermissions.any {
                ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, bluetoothPermissions, 1)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                enableBluetooth()
            } else {
                Toast.makeText(this, "Bluetooth permissions required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun configurePlayGameButton() {
        val button = findViewById<Button>(R.id.game_button)
        button.setOnClickListener {
            if(opponentName != "PC") {
                checkBluetoothPermissions()
                if (bluetoothAdapter?.isEnabled != true) {
                    enableBluetooth()


                }
              //  CoroutineScope(Dispatchers.IO).launch {
                startBluetoothConnection()

              //  }
                //val intent =  Intent(this,MainActivity::class.java)
                //gameSettings = GameSettings(opponentName, playerColor)

                //startActivity(intent)
            } else {
                val intent =  Intent(this,MainActivity::class.java)
                gameSettings = GameSettings(opponentName, playerColor)

                startActivity(intent)
            }
        }

    }
    private fun startBluetoothConnection() {

        startServer()
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
                        else -> "Random"
                    }
                    playerColor = selectedColor
                    opponentName = "PC"
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
            opponentName = chosenOpponent
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }


    private fun startServer() {




        try {
            Log.d("Bluetooth", "Server started, waiting for connection...")
            socket = serverSocket?.accept()

            socket?.let {
                Log.d("Bluetooth", "Client connected")
                manageConnectedSocket(it)
            }
        } catch (e: IOException) {
            Log.e("Bluetooth", "Error accepting connection", e)
        } finally {
            try {
                serverSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error closing server socket", e)
            }
        }
    }
    private fun manageConnectedSocket(socket: BluetoothSocket) {
        val inputStream: InputStream = socket.inputStream
        val outputStream: OutputStream = socket.outputStream
        val bufferedReader = BufferedReader(InputStreamReader(socket.inputStream))
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Send GameSettings to the client
                gameSettings = GameSettings(opponentName, playerColor)
                val serializedGameSettings = serializeGameSettings(gameSettings)
               // outputStream.write(serializedGameSettings)

                // Listen for response from client
               // val buffer = ByteArray(1024)
                //var bytes = inputStream.read(buffer)
                var message  = bufferedReader.readLine()
                while(message != null) {
                    message = bufferedReader.readLine()
                }



                Log.d("Bluetooth", "Received message: $message")
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error managing socket", e)
            }
        }
    }
    private fun serializeGameSettings(gameSettings: GameSettings): ByteArray {
        // Parcel the GameSettings object
        val parcel = Parcel.obtain()
        gameSettings.writeToParcel(parcel, 0)
        val serializedGameSettings = parcel.marshall()
        parcel.recycle()
        return serializedGameSettings
    }
    private fun deserializeGameSettings(data: ByteArray): GameSettings {
        val parcel = Parcel.obtain()
        parcel.unmarshall(data, 0, data.size)
        parcel.setDataPosition(0)
        val gameSettings = GameSettings.CREATOR.createFromParcel(parcel)
        parcel.recycle()
        return gameSettings
    }

    private fun showChallengeDialog(gameSettings: GameSettings) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this@main_menu)
            builder.setTitle("Game Challenge")
            builder.setMessage("Opponent: ${gameSettings.opponentName} wants to play.\nColor: ${gameSettings.color}")

            // Allow user to name their opponent
            val input = EditText(this@main_menu)
            builder.setView(input)

            builder.setPositiveButton("Accept") { dialog, _ ->
                opponentName = input.text.toString()
                dialog.dismiss()
            }

            builder.setNegativeButton("Decline") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

}