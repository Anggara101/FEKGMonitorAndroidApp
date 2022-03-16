package com.anggara.fekgmonitor.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anggara.fekgmonitor.ui.component.GraphCard
import com.anggara.fekgmonitor.ui.component.HomeTopBar
import com.anggara.fekgmonitor.ui.component.TitleCard

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
//    val bluetoothState by homeViewModel.stateBluetooth.observeAsState()
    val subtitle = homeViewModel.homeSubtitle.value

    Scaffold(topBar = { HomeTopBar(navController, homeViewModel) }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleCard(subtitle = subtitle)
            GraphCard()
            Button(
                onClick = { homeViewModel.onStartButtonClick() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Start")
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(navController = rememberNavController())
}