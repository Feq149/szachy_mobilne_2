package com.example.szachy_mobilne_2.FullGameControl

import com.example.szachy_mobilne_2.View.ChessView

import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

class GameBluetoothController(
    chessView: ChessView,
    private val socket: BluetoothSocket?
) : GameController(chessView) {

    private val inputStream: InputStream = socket!!.inputStream
    private val outputStream: OutputStream = socket!!.outputStream

    init {
        // Listen for opponent's moves in a coroutine
        if (!userIsWhite) {
            getOpponentMovePlayed()
        }
    }

    // Override the makeAMove method to send the move over Bluetooth
    override fun makeAMove(move: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
        if (game.makeAMove(move)) {
            // Add the move to game history
            gameHistory += moveToStringConverter(move)

            // Send the move to the opponent over Bluetooth
            sendMove(move)

            // If the game is finished, perform finishing operations
            if (game.isGameFinished) {
                performFinishingOperations()
            }

            return true
        }
        return false
    }

    // Override the getOpponentMovePlayed method to receive the move via Bluetooth
    override fun getOpponentMovePlayed() {
        if (game.isGameFinished) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Wait for the opponent's move
                val opponentMove = receiveMove()
                opponentMove?.let { move ->
                    // Make the opponent's move on the local game
                    val moveWasMade = game.makeAMove(move)

                    // Update game history and UI
                    if (moveWasMade) {
                        gameHistory += moveToStringConverter(move)
                        chessView.invalidate()

                        // Enable player to make the next move if the game isn't finished
                        if (!game.isGameFinished) {
                            chessView.enableMove = true
                        } else {
                            performFinishingOperations()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Bluetooth", "Error receiving opponent's move", e)
            }
        }
    }

    // Helper function to send move via Bluetooth
    private fun sendMove(move: Pair<Pair<Int, Int>, Pair<Int, Int>>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Serialize move into four integers (x1, y1, x2, y2)
                val moveData = ByteArray(16)
                val byteBuffer = java.nio.ByteBuffer.wrap(moveData)
                byteBuffer.putInt(move.first.first)
                byteBuffer.putInt(move.first.second)
                byteBuffer.putInt(move.second.first)
                byteBuffer.putInt(move.second.second)

                // Send the move over Bluetooth
                outputStream.write(moveData)
                outputStream.flush()
            } catch (e: Exception) {
                Log.e("Bluetooth", "Error sending move", e)
            }
        }
    }

    // Helper function to receive move via Bluetooth
    private fun receiveMove(): Pair<Pair<Int, Int>, Pair<Int, Int>>? {
        try {
            val buffer = ByteArray(16)
            val bytesRead = inputStream.read(buffer)

            if (bytesRead != -1) {
                val byteBuffer = java.nio.ByteBuffer.wrap(buffer)
                val x1 = byteBuffer.getInt()
                val y1 = byteBuffer.getInt()
                val x2 = byteBuffer.getInt()
                val y2 = byteBuffer.getInt()

                // Return the move as a pair of coordinates
                return Pair(Pair(x1, y1), Pair(x2, y2))
            }
        } catch (e: Exception) {
            Log.e("Bluetooth", "Error receiving move", e)
        }

        return null
    }
}
