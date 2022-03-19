package com.anggara.fekgmonitor.ui.history

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@ExperimentalMaterialApi
@Composable
fun HistoryScreen(navController: NavController, historyViewModel: HistoryViewModel = viewModel()) {
    val fileList = historyViewModel.fileList.value
    Scaffold(topBar = { HistoryTopBar(navController = navController)}) {innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(fileList) { fileName ->
                ListItem(
                    text = { Text(fileName) },
                    modifier = Modifier.clickable {
                        Log.i("History Screen", "$fileName Clicked")
                    }
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun HistoryPreview() {
    HistoryScreen(navController = rememberNavController())
}