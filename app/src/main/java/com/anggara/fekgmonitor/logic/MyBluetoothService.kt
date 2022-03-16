package com.anggara.fekgmonitor.logic

import android.bluetooth.BluetoothAdapter

class MyBluetoothService {
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val pairedDevices = bluetoothAdapter.bondedDevices
    private val listOfDeviceName: ArrayList<String> = ArrayList()

    fun getListOfPairedDevices(): List<String>{
        pairedDevices.forEach { device ->
            listOfDeviceName.add(device.name)
        }
        return listOfDeviceName
    }
}