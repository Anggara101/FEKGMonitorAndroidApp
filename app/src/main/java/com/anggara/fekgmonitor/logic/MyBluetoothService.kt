package com.anggara.fekgmonitor.logic

import android.bluetooth.BluetoothAdapter

class MyBluetoothService {
    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val listOfDeviceName: ArrayList<String> = ArrayList()

    fun getListOfPairedDevices(): List<String>{
        val pairedDevices = bluetoothAdapter.bondedDevices
        pairedDevices.forEach { device ->
            listOfDeviceName.add(device.name)
        }
        return listOfDeviceName
    }

}