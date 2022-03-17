package com.anggara.fekgmonitor.ui.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anggara.fekgmonitor.logic.MyBluetoothService

class HomeViewModel : ViewModel() {
    private val tag = "View Model"

    private val _stateBluetooth = MutableLiveData(false)
    val stateBluetooth: LiveData<Boolean> = _stateBluetooth

    private val _homeTitle = mutableStateOf("Bluetooth Off")
    val homeTitle: MutableState<String> = _homeTitle
    private val _homeSubtitle = mutableStateOf("Bluetooth required, please enable Bluetooth")
    val homeSubtitle: MutableState<String> = _homeSubtitle

    private val _selectedDevice = mutableStateOf("rapsberrypi")
    val selectedDevice: MutableState<String> =_selectedDevice

    private val myBluetoothService = MyBluetoothService()
    private val _listOfPairedDevices = mutableStateOf(listOf("raspberrypi, ESP32test"))
    val listOfPairedDevice: State<List<String>> = _listOfPairedDevices

    private var numListDeviceCalled = 0

    init {
        _stateBluetooth.value = myBluetoothService.bluetoothAdapter.isEnabled
        if(_stateBluetooth.value==true){
            _homeTitle.value = "Bluetooth Enabled"
//            _homeSubtitle.value = "Press start to connect to device"
            _homeSubtitle.value = "Press start to connect to ${_selectedDevice.value}"
            _listOfPairedDevices.value = myBluetoothService.getListOfPairedDevices()
            numListDeviceCalled++
        }
    }

    fun onBluetoothStateChanged(newStateBluetooth: Boolean){
        _stateBluetooth.value = newStateBluetooth
        if (_stateBluetooth.value == true){
            if (numListDeviceCalled<1) {
                _listOfPairedDevices.value = myBluetoothService.getListOfPairedDevices()
                numListDeviceCalled++
            }
            _homeTitle.value = "Bluetooth on"
//            _homeSubtitle.value = "Press start to connect to device"
            _homeSubtitle.value = "Press start to connect to ${_selectedDevice.value}"
        }else{
            _homeTitle.value = "Bluetooth off"
            _homeSubtitle.value = "Bluetooth required, please enable Bluetooth"
        }
        Log.i(tag, "bluetoothStateChanged: ${_listOfPairedDevices}, $numListDeviceCalled")
    }

    override fun onCleared() {
        super.onCleared()
        numListDeviceCalled=0
    }

    fun onStartButtonClick(){
        _homeSubtitle.value = "Start Button Clicked"
//        Log.i("View Model", "buttonClicked: $stateBluetooth")
    }

    fun onDeviceClick(){

    }

    fun onSelectedDevice(deviceName: String){
        _selectedDevice.value = deviceName
        _homeSubtitle.value = "Press start to connect to ${_selectedDevice.value}"
    }
}