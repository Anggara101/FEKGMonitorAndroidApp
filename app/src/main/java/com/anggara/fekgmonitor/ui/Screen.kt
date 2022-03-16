package com.anggara.fekgmonitor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val icon: ImageVector) {
    Home(
        icon = Icons.Default.Home
    ),
    History(
        icon = Icons.Default.History
    );
    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                Home.name -> Home
                History.name -> History
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}