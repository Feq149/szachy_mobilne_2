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
import androidx.core.app.ActivityCompat
import com.example.szachy_mobilne_2.FullGameControl.GameSettings
import com.example.szachy_mobilne_2.database.DatabaseOfGames
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.os.Parcel
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.UUID
import kotlin.concurrent.thread

var database : DatabaseOfGames? = null
var gameSettings = GameSettings("PC","Random")
var socket: BluetoothSocket? = null

val bluetoothPermissions = arrayOf(
    Manifest.permission.BLUETOOTH,
    Manifest.permission.BLUETOOTH_ADMIN,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT

)
 val uuid: UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")
class main_menu<BluetoothServerSocket> : AppCompatActivity(),IncomingGameListener {
    val opponents = mutableListOf("PC","Online")
    var opponentName = "PC"
    var playerColor = "Random"
    private val appName = "Mobilne_Szachy"

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    var serverSocket: android.bluetooth.BluetoothServerSocket? = null
    val eventListener : IncomingGameListener = this
    var areWeTheClient = false
    var challengeInProgress = false
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

    override fun onEventTriggered(event: ChallengeAcceptedEvent) {
        val intent =  Intent(this,MainActivity::class.java)


        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (bluetoothPermissions.any {
                ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, bluetoothPermissions, 1)
        }
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


    private fun startClient() {
        var foundConnection = false
        if (bluetoothPermissions.any {
                ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, bluetoothPermissions, 1)
        }
        try {

            var pairedDevices: Set<BluetoothDevice>? = null

            pairedDevices = bluetoothAdapter?.bondedDevices

            pairedDevices?.forEach { device ->
                val connectThread = ConnectThread(device, this, this)
                //connectThread.start()
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address

                if (deviceName.contains("Galaxy")) {
                    socket =
                        device.createRfcommSocketToServiceRecord(uuid)
                    connectThread.start()
                    challengeInProgress = true
                    foundConnection = true
                }

                val i = 6;
                //return
            }

        } catch (e: SecurityException) {
            challengeInProgress = false
        } finally {
            if(!foundConnection) {
                challengeInProgress = false
            }
        }
    }
    override fun onEventTriggered(event: IncomingGameEvent) {
        showChallengeDialog(event.color)
        //startClient()

        //manageConnectedSocket(socket!!)
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
                    areWeTheClient = false
                    challengeInProgress = true
                    manageConnectedSocket(socket!!)


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
    fun sendMessageViaSocket(message:String) {
        try {
            OutputStreamWriter(socket!!.outputStream, StandardCharsets.UTF_8).use { writer ->
                writer.write(message + "\n")
                writer.flush()

            }
        } catch (e: IOException) {
            val i = 5
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    fun configurePlayGameButton() {
        val button = findViewById<Button>(R.id.game_button)
        button.setOnClickListener {
            if(challengeInProgress) {
                return@setOnClickListener
            }
            if(opponentName != "PC") {
                challengeInProgress = true
                checkBluetoothPermissions()
                if (bluetoothAdapter?.isEnabled != true) {
                    enableBluetooth()


                }
              //  CoroutineScope(Dispatchers.IO).launch {
                areWeTheClient = true
                startClient()
                if(!challengeInProgress) {
                    opponentName = "PC"
                    playerColor = "Random"
                    Toast.makeText(this,"no connection established", Toast.LENGTH_LONG).show()
                    return@setOnClickListener

                }
                var message = "Random\n"
                if(playerColor == "White") {
                    message = "Black\n"
                }
                if(playerColor == "Black") {
                    message = "White\n"
                }
                sendMessageViaSocket(message)
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
                    showAddOpponentDialog()
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
    private fun showAddOpponentDialog() {
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

               // outputStream.write(serializedGameSettings)

                // Listen for response from client
               // val buffer = ByteArray(1024)
                //var bytes = inputStream.read(buffer)
                var message  = bufferedReader.readLine()
                if(message == null) {
                    opponentName = "PC"
                    playerColor = "Random"
                    socket.close();
                }
                eventListener.onEventTriggered(IncomingGameEvent(message))
                if(areWeTheClient) {
                    if(message != "ok") {
                        Log.d("messages","failed to receive 'ok' confirmation")
                        opponentName = "PC"
                        playerColor = "Random"
                        socket.close();
                        areWeTheClient = false
                        return@launch
                    } else {

                        eventListener.onEventTriggered(ChallengeAcceptedEvent())
                        return@launch
                    }
                }
                if(message.equals("Random") || message.equals("Black") || message.equals("White")) {
                    playerColor = message
                    showChallengeDialog(message)
                } else {
                    Log.d(tag, "$message is not a message in valid format")
                    opponentName = "PC"
                    playerColor = "Random"
                    socket.close();
                    challengeInProgress = false
                }



                Log.d("Bluetooth", "Received message: $message")
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error managing socket", e)
                opponentName = "PC"
                playerColor = "Random"
                challengeInProgress = false
            }
        }
    }


    private fun showChallengeDialog(colorToPlay : String) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this@main_menu)
            builder.setTitle("Game Challenge")
            builder.setMessage("Opponent wants to play.\nColor: $colorToPlay")

            // Allow user to name their opponent
            val input = EditText(this@main_menu)
            builder.setView(input)

            builder.setPositiveButton("Accept") { dialog, _ ->
                opponentName = input.text.toString()
                if(opponentName.all {it == ' ' } || opponentName == "" || opponentName == "PC" || opponentName.length > 20) {
                    opponentName = "Random"
                }
                gameSettings.color = colorToPlay;
                gameSettings.opponentName = opponentName;
                sendMessageViaSocket("ok\n")
                this.onEventTriggered(ChallengeAcceptedEvent())

                dialog.dismiss()
            }

            builder.setNegativeButton("Decline") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }
    }

}