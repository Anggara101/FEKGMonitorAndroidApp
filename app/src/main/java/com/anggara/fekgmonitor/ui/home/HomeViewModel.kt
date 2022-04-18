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

    // Live State
    private val _stateBluetooth = MutableLiveData(false)
    val stateBluetooth: LiveData<Boolean> = _stateBluetooth

    // Live State
    private val _stateConnection = MutableLiveData(false)
    val stateConnection: LiveData<Boolean> = _stateConnection

    // Graph Visibility
    private val _graphVisibility = MutableLiveData(false)
    val graphVisibility: LiveData<Boolean> = _graphVisibility

    // Live Ecg Data
    private val _ecgLiveData = mutableStateOf(arrayListOf<Float>())
    val ecgLiveData: MutableState<ArrayList<Float>> = _ecgLiveData

    // UI VM
    private val _homeTitle = mutableStateOf("Bluetooth Off")
    val homeTitle: MutableState<String> = _homeTitle
    private val _homeSubtitle = mutableStateOf("Bluetooth required, please enable Bluetooth")
    val homeSubtitle: MutableState<String> = _homeSubtitle

    // Bluetooth VM
    private val _selectedDevice = mutableStateOf("raspberrypi")
    val selectedDevice: MutableState<String> =_selectedDevice

    private val myBluetoothService = MyBluetoothService(this)
    private val _listOfPairedDevices = mutableStateOf(listOf("raspberrypi, ESP32test"))
    val listOfPairedDevice: State<List<String>> = _listOfPairedDevices

    private var numListDeviceCalled = 0

    init {
        _stateBluetooth.value = myBluetoothService.bluetoothAdapter.isEnabled
        if(_stateBluetooth.value==true){
            _homeTitle.value = "Bluetooth On"
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
            _homeTitle.value = "Bluetooth On"
//            _homeSubtitle.value = "Press start to connect to device"
            _homeSubtitle.value = "Press start to connect to ${_selectedDevice.value}"
        }else{
            _homeTitle.value = "Bluetooth Off"
            _homeSubtitle.value = "Bluetooth required, please enable Bluetooth"
        }
        Log.i(tag, "bluetoothStateChanged: ${_listOfPairedDevices}, $numListDeviceCalled")
    }

    fun onConnectionStateChanged(newConnectionState: Boolean){
        _stateConnection.value = newConnectionState
        if (_stateConnection.value == true && myBluetoothService.getState()==myBluetoothService.STATE_CONNECTED){
            _homeTitle.value = "Connected"
            _homeSubtitle.value = "Connected with ${_selectedDevice.value}"
        }else{
            _homeTitle.value = "Disconnected"
            _homeSubtitle.value = "Disconnected with ${_selectedDevice.value}"
        }
    }

    override fun onCleared() {
        super.onCleared()
        numListDeviceCalled=0
    }

    fun onStartButtonClick(){
        if (_stateBluetooth.value == true){
            when {
                myBluetoothService.getState()==myBluetoothService.STATE_NONE -> {
                    _homeSubtitle.value = "Connecting to ${_selectedDevice.value}"
                    myBluetoothService.connect(_selectedDevice.value)
                }
                myBluetoothService.getState()==myBluetoothService.STATE_CONNECTED -> {
//                    _homeSubtitle.value = "Connected with ${_selectedDevice.value}"
                    myBluetoothService.stop()
                }
            }

        }else{
            _homeSubtitle.value = "Enable Bluetooth first"
        }
//        Log.i("View Model", "buttonClicked: $stateBluetooth")
    }

    fun onSelectedDevice(deviceName: String){
        _selectedDevice.value = deviceName
        _homeSubtitle.value = "Press start to connect to ${_selectedDevice.value}"
    }

    fun onRead(readBytes: Int, readBuf: ByteArray){
        val readMessage = String(readBuf, 0, readBytes)
        var outMessage: String
        var ecgRaw: Float

        if (readMessage.contains("\n")){
            val token = readMessage.split("\r\n")
            token.forEach {
                outMessage = it
                if (outMessage.isNotEmpty()){
                    try {
                        ecgRaw = outMessage.toFloat()
                        _ecgLiveData.value.add(ecgRaw)
                        Log.i("View Model", ecgRaw.toString())
                    } catch (e: Exception){
                        Log.e("View Model", "Parsing error $outMessage", e)
                    }
                }
            }
        } else{
            outMessage = readMessage
            try {
                ecgRaw = outMessage.toFloat()
                _ecgLiveData.value.add(ecgRaw)
                Log.i("View Model", ecgRaw.toString())
            } catch (e: Exception){
                Log.e("View Model", "Parsing error $outMessage", e)
            }
        }
    }

    fun onDestroy(){
        myBluetoothService.stop()
    }
}