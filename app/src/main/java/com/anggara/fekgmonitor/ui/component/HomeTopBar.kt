package com.anggara.fekgmonitor.ui.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anggara.fekgmonitor.ui.Screen
import com.anggara.fekgmonitor.ui.home.HomeViewModel

@Composable
fun HomeTopBar(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    var expanded by remember{mutableStateOf(false)}
    val radioOptions = homeViewModel.listOfPairedDevice.value
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    TopAppBar(
        title = { Text("Simple TopAppBar") },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { navController.navigate(Screen.History.name) }) {
                Icon(Icons.Filled.History, contentDescription = "Go to History Screen")
            }
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Filled.Devices, contentDescription = "Show List of Devices")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded=false },
                modifier = Modifier.selectableGroup()
                ) {
                radioOptions.forEach{ text ->
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) ; homeViewModel.onSelectedDevice(text)},
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text);
                                homeViewModel.onSelectedDevice(text)
                            }
                        )
                        Text(text = text)
                    }
                }
            }
        }
    )
}