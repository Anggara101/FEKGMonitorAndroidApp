package com.anggara.fekgmonitor.ui.graph

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anggara.fekgmonitor.ui.component.MPLineChart
import com.anggara.fekgmonitor.ui.history.HistoryViewModel

@Composable
fun GraphScreen(
    historyViewModel: HistoryViewModel = viewModel()
) {
    val rawEcgList = historyViewModel.rawEcgList.value
    MPLineChart(rawEcg = rawEcgList)
}