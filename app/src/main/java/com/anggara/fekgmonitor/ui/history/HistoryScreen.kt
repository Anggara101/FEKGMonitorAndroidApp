package com.anggara.fekgmonitor.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.anggara.fekgmonitor.ui.component.HistoryTopBar
import com.anggara.fekgmonitor.ui.home.HomeViewModel

@ExperimentalMaterialApi
@Composable
fun HistoryScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    Scaffold(topBar = { HistoryTopBar(navController = navController)}) {innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ListItem(
                text = { Text("Two line list item") },
                secondaryText = { Text("Secondary text") }
            )
            ListItem(
                text = { Text("Two line list item") },
                secondaryText = { Text("Secondary text") }
            )
            ListItem(
                text = { Text("Two line list item") },
                secondaryText = { Text("Secondary text") }
            )

        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun HistoryPreview() {
    HistoryScreen(navController = rememberNavController())
}