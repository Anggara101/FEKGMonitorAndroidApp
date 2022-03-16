package com.anggara.fekgmonitor.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.anggara.fekgmonitor.ui.Screen

@Composable
fun HistoryTopBar(
    navController: NavController
) {
    TopAppBar(
        title = { Text("History") },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(Screen.Home.name){
                popUpTo(Screen.Home.name){
                    inclusive = true
                }
            } }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to home screen"
                )
            }
        }
    )
}
