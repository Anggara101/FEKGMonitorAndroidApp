package com.anggara.fekgmonitor.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitleCard(subtitle: String) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), elevation = 4.dp) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Title",
                style = MaterialTheme.typography.h4,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.subtitle1,

            )
        }

    }
}