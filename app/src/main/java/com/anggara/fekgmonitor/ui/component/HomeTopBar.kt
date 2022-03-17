package com.anggara.fekgmonitor.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
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
                Icon(Icons.Filled.History, contentDescription = "Go to History Screen")
            }
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(Icons.Filled.Devices, contentDescription = "Show List of Devices")
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

@Composable
fun DropDownDeviceItem(homeViewModel: HomeViewModel) {
    val bluetoothState = homeViewModel.stateBluetooth.value
    val radioOptions = homeViewModel.listOfPairedDevice.value
    if(bluetoothState == true) {
        val (selectedOption, onOptionSelected) = rememberSaveable{
            mutableStateOf(radioOptions[0])
        }
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            homeViewModel.onSelectedDevice(text)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = {
                        onOptionSelected(text)
                        homeViewModel.onSelectedDevice(text)
                    }
                )
                Text(text = text)
            }
        }
    }else{
        Text(text = "Please enable Bluetooth")
    }
}