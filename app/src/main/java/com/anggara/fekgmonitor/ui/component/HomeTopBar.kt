package com.anggara.fekgmonitor.ui.component

import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anggara.fekgmonitor.ui.Screen
import com.anggara.fekgmonitor.ui.home.HomeViewModel

@Composable
fun HomeTopBar(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val listOfPairedDevice = homeViewModel.listOfPairedDevice.value
    var expanded by remember{mutableStateOf(false)}
    TopAppBar(
        title = { Text("FEKG Monitor") },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { navController.navigate(Screen.History.name) }) {
                Icon(Icons.Default.History, contentDescription = "Go to History Screen")
            }
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(Icons.Default.Devices, contentDescription = "Show List of Devices")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded=false},
                modifier = Modifier.selectableGroup()
            )
            {
                if(homeViewModel.stateBluetooth.value == true) {
                    listOfPairedDevice.forEach { text ->
                        DropdownMenuItem(onClick = {
                            homeViewModel.onSelectedDevice(text)
                            expanded = false
                        }) {
                            Text(text = text)
                        }
                    }
                }else{
                    DropdownMenuItem(
                        enabled = false,
                        onClick = {}
                    ) {
                        Text(text = "Please enable Bluetooth")
                    }
                }
            }
        }
    )
}
