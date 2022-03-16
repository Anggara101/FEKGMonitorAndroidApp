package com.anggara.fekgmonitor.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GraphCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "MHR",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "86 bpm",
                    style = MaterialTheme.typography.h5,
                    )
            }
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "FHR",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "124 bpm",
                    style = MaterialTheme.typography.h5,

                    )
            }
        }
    }
}