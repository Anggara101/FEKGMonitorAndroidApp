package com.anggara.fekgmonitor.logic

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class MyBluetoothService {
    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val listOfDeviceName: ArrayList<String> = ArrayList()
    val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun getListOfPairedDevices(): List<String>{
        val pairedDevices = bluetoothAdapter.bondedDevices
        pairedDevices.forEach { device ->
            listOfDeviceName.add(device.name)
        }
        return listOfDeviceName
    }

    fun getDeviceAddress(deviceName: String): String{
        val pairedDevices = bluetoothAdapter.bondedDevices
        var deviceAddress = "00:11:22:33:AA:BB"
        pairedDevices.forEach { device ->
            deviceAddress = if (device.name == deviceName){
                device.address
            }else{
                "00:11:22:33:AA:BB"
            }
        }
        return deviceAddress
    }

    inner class ConnectThread(deviceName: String) : Thread() {
        private val deviceAddress = getDeviceAddress(deviceName)
        private val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.

                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                Log.i("Bluetooth Service", "Connected to $deviceAddress")
                manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth Service", "Could not close the client socket", e)
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        val outputStream = socket.outputStream
        val data = "1\n"
        outputStream.write(data.toByteArray())
        Log.i("Bluetooth Service", "data sent")
    }



}