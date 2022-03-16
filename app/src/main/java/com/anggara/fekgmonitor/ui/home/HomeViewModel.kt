package com.anggara.fekgmonitor.ui.home

import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anggara.fekgmonitor.logic.MyBluetoothService

class HomeViewModel : ViewModel() {

    private val _stateBluetooth = MutableLiveData(false)
    val stateBluetooth: LiveData<Boolean> = _stateBluetooth

    private val _homeSubtitle = mutableStateOf("Press start button to start")
    val homeSubtitle: MutableState<String> = _homeSubtitle

    private val _selectedDevice = mutableStateOf("rapsberrypi")
    val selectedDevice: MutableState<String> =_selectedDevice

    private val myBluetoothService = MyBluetoothService()
    private val _listOfPairedDevices = mutableStateOf(listOf("raspberrypi, ESP32test"))
    val listOfPairedDevice: State<List<String>> = _listOfPairedDevices

    init {
        try {
            _listOfPairedDevices.value = myBluetoothService.getListOfPairedDevices()
        } catch (e: Exception) {
            Log.e("View Model", "Error getting device", e)
        }

    }

    fun onBluetoothStateChanged(newStateBluetooth: Boolean){
        _stateBluetooth.value = newStateBluetooth
        if (_stateBluetooth.value == true){
            _homeSubtitle.value = "Bluetooth is enabled, press start to connect to device"
            _listOfPairedDevices.value = myBluetoothService.getListOfPairedDevices()
        }else{
            _homeSubtitle.value = "Bluetooth is disabled, please enable bluetooth"
        }
//        Log.i("View Model", "bluetoothStateChanged: $stateBluetooth")
    }

    fun onStartButtonClick(){
        _homeSubtitle.value = "Start Button Clicked"
//        Log.i("View Model", "buttonClicked: $stateBluetooth")
    }

    fun onSelectedDevice(deviceName: String){
        _selectedDevice.value = deviceName
        Log.i("View Model", "Device Selected: $selectedDevice")
    }
}