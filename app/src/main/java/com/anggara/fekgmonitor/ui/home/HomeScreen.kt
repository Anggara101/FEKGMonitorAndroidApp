package com.anggara.fekgmonitor.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anggara.fekgmonitor.ui.component.GraphCard
import com.anggara.fekgmonitor.ui.component.HomeTopBar
import com.anggara.fekgmonitor.ui.component.MPLineChart
import com.anggara.fekgmonitor.ui.component.TitleCard

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
//    val bluetoothState by homeViewModel.stateBluetooth.observeAsState()
    val title = homeViewModel.homeTitle.value
    val subtitle = homeViewModel.homeSubtitle.value

    val graphCardVisibility by homeViewModel.graphVisibility.observeAsState()

    Scaffold(topBar = { HomeTopBar(navController, homeViewModel) }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleCard(title = title, subtitle = subtitle)
            AnimatedVisibility(
                visible = graphCardVisibility == true
            ) {
                Column() {
                    GraphCard()
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                        homeViewModel.onStartButtonClick()
//                        graphCardVisibility = !graphCardVisibility
                    }
                ) {
                    Text(text = "Start")
                }
            }
        }
    }
}


@Preview
@Composable
fun HomePreview() {
    HomeScreen(navController = rememberNavController(), homeViewModel = viewModel())
}