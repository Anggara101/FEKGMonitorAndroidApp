package com.anggara.fekgmonitor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MultilineChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val icon: ImageVector) {
    Home(
        icon = Icons.Default.Home
    ),
    History(
        icon = Icons.Default.History
    ),
    Graph(
        icon = Icons.Default.MultilineChart
    );
    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                Home.name -> Home
                History.name -> History
                Graph.name -> Graph
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}