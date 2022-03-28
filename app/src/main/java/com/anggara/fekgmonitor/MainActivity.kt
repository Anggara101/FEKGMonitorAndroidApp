package com.anggara.fekgmonitor

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.anggara.fekgmonitor.data.RawEcgData
import com.anggara.fekgmonitor.logic.FEKGNavHost
import com.anggara.fekgmonitor.logic.MyBluetoothService
import com.anggara.fekgmonitor.ui.history.HistoryViewModel
import com.anggara.fekgmonitor.ui.home.HomeViewModel
import com.anggara.fekgmonitor.ui.theme.FEKGMonitorTheme
import java.io.IOException
import java.sql.Connection


class MainActivity : ComponentActivity() {
    val homeViewModel by viewModels<HomeViewModel>()
    private val historyViewModel by viewModels<HistoryViewModel>()
    private val bluetoothStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action: String? = intent.action
            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    BluetoothAdapter.STATE_OFF -> {
                        Log.i("Bluetooth Service", "Bluetooth Off")
                        homeViewModel.onBluetoothStateChanged(false)
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        Log.i("Bluetooth Service", "Bluetooth Turning Off")
                    }
                    BluetoothAdapter.STATE_ON -> {
                        Log.i("Bluetooth Service", "Bluetooth On")
                        homeViewModel.onBluetoothStateChanged(true)
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        Log.i("Bluetooth Service", "Bluetooth Turning On")
                    }
                }
            }
        }
    }

    private val connectionStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action: String? = intent.action
            if (action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                Log.i("Bluetooth Service", "Connected")
                homeViewModel.onConnectionStateChanged(true)
            } else if(action == BluetoothDevice.ACTION_ACL_DISCONNECTED){
                Log.i("Bluetooth Service", "Disconnected")
                homeViewModel.onConnectionStateChanged(false)
            }
        }
    }

    private var rawEcgData = RawEcgData("", "")
    private val rawEcgList: ArrayList<RawEcgData> = arrayListOf(rawEcgData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filterStateChanged = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothStateReceiver, filterStateChanged)
        val filterACLConnected = IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED)
        val filterACLDisconnected = IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED)

        registerReceiver(connectionStateReceiver, filterACLConnected)
        registerReceiver(connectionStateReceiver, filterACLDisconnected)
        readRawCSV()
        saveCSV("data4raw.csv", rawEcgList, application.applicationContext)
        historyViewModel.setRawEcgList(rawEcgList)
        setContent {
            FEKGApp(homeViewModel, historyViewModel)
        }
    }


    fun connectionChanged(){
        val stateConnection = homeViewModel.stateConnection.value
        homeViewModel.onConnectionStateChanged(!stateConnection!!)
    }

    private fun readRawCSV(){
        val inputStream = resources.openRawResource(R.raw.data4)
        val bufferedReader = inputStream.bufferedReader()
        var line = ""
        try {
            while (bufferedReader.readLine() != null){
                line = bufferedReader.readLine()
                val token = line.split(",")
                rawEcgData = if(token[0]== ""){
                    RawEcgData("0", token[1])
                }else if (token[1] == ""){
                    RawEcgData(token[0], "0")
                }else if (token[0]== "" && token[1] == ""){
                    RawEcgData(token[0], "0")
                }else{
                    RawEcgData(token[0], token[1])
                }

                rawEcgList.add(rawEcgData)
//                Log.i("Main Activity", "read $line")
            }
        } catch (e: Exception) {
            Log.e("Main Activity", "error: $line", e)
        }
    }

    fun saveCSV(filename:String, rawEcgList: ArrayList<RawEcgData>, context: Context){
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {outputStream ->
            val bufferedWriter = outputStream.bufferedWriter()
            rawEcgList.forEach { line ->
                try {
                    bufferedWriter.write("${line.t},${line.ecgRaw}")
                    bufferedWriter.newLine()
//                        Log.i("Main Activity", "write $line")
                } catch (e: IOException) {
                    Log.e("Main Activity", "error: $line", e)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewModel.onDestroy()
        unregisterReceiver(bluetoothStateReceiver)
        unregisterReceiver(connectionStateReceiver)
    }
}

@Composable
fun FEKGApp(homeViewModel: HomeViewModel, historyViewModel: HistoryViewModel) {
    FEKGMonitorTheme {
        val navController = rememberNavController()
        
        FEKGNavHost(navController = navController, homeViewModel, historyViewModel)
    }
    
}


