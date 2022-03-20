package com.anggara.fekgmonitor.logic

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anggara.fekgmonitor.ui.Screen
import com.anggara.fekgmonitor.ui.graph.GraphScreen
import com.anggara.fekgmonitor.ui.history.HistoryScreen
import com.anggara.fekgmonitor.ui.history.HistoryViewModel
import com.anggara.fekgmonitor.ui.home.HomeViewModel
import com.anggara.fekgmonitor.ui.home.HomeScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FEKGNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    historyViewModel: HistoryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.name,
    ) {
        composable(Screen.Home.name) {
            HomeScreen(navController, homeViewModel)
        }
        composable(Screen.History.name) {
            HistoryScreen(navController, historyViewModel)
        }
        composable(Screen.Graph.name) {
            GraphScreen(historyViewModel = historyViewModel)
        }
    }

}