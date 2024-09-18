package com.example.szachy_mobilne_2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets.UTF_8
import java.util.UUID
import android.Manifest
class ConnectThread  constructor(
    device: BluetoothDevice,
    context: Context,
    activity: AppCompatActivity
) :
    Thread() {
    private val mmSocket: BluetoothSocket?
    private val mmDevice: BluetoothDevice
    private val context: Context
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val activity: AppCompatActivity

    init {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        var tmp: BluetoothSocket? = null
        this.context = context
        this.activity = activity
        val bluetoothPermissions = arrayOf<String>(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,

        )
        var missingPermissions = false
        for (permission in bluetoothPermissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions = true
            }
        }
        if (missingPermissions) {
            ActivityCompat.requestPermissions(activity, bluetoothPermissions, 1)
        }
        mmDevice = device
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp =
                device.createRfcommSocketToServiceRecord(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"))
        } catch (e: IOException) {
            Log.e(TAG, "Socket's create() method failed", e)
        }
        mmSocket = tmp
    }

    override fun run() {
        val bluetoothPermissions = arrayOf<String>(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        var missingPermissions = false
        for (permission in bluetoothPermissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions = true
            }
        }
        if (missingPermissions) {
            ActivityCompat.requestPermissions(activity, bluetoothPermissions, 1)
        }
        // Cancel discovery because it otherwise slows down the connection.
        try {
            bluetoothAdapter.cancelDiscovery()
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket!!.connect()
        } catch (connectException: IOException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket!!.close()
            } catch (closeException: IOException) {
                Log.e(TAG, "Could not close the client socket", closeException)
            }
            return
        } catch (e: Exception) {
            val i = 6
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
       // manageMyConnectedSocket(mmSocket, "Hello")
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }

    public fun manageMyConnectedSocket(mmSocket: BluetoothSocket?, message : String) {
        try {
            OutputStreamWriter(mmSocket!!.outputStream, UTF_8).use { writer ->
                writer.write(message + "\n")
                writer.flush()

            }
        } catch (e: IOException) {
            val i = 5
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        println(mmSocket!!.isConnected)
    }
}