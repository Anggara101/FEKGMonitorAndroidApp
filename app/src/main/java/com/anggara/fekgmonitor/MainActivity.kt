package com.anggara.fekgmonitor

import android.bluetooth.BluetoothAdapter
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
import com.anggara.fekgmonitor.logic.FEKGNavHost
import com.anggara.fekgmonitor.ui.home.HomeViewModel
import com.anggara.fekgmonitor.ui.theme.FEKGMonitorTheme


class MainActivity : ComponentActivity() {
    private val homeViewModel by viewModels<HomeViewModel>()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter1 = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothStateReceiver, filter1)
        setContent {
            FEKGApp(homeViewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothStateReceiver)
    }
}

@Composable
fun FEKGApp(homeViewModel: HomeViewModel) {
    FEKGMonitorTheme {
        val navController = rememberNavController()
        
        FEKGNavHost(navController = navController, homeViewModel)
    }
    
}


