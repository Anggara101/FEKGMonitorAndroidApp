package com.anggara.fekgmonitor.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GraphCard(mode:Int, ecgRaw:Float, fHR:Float, mHR:Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 2.dp,
    ) {
        if (mode == 1){
            Column {
                Text(text = "Training", style = MaterialTheme.typography.h5)
                LineChartLive(mode, value = ecgRaw)
            }
        }else if (mode == 2){
            Column {
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
                            text = "$mHR bpm",
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
                            text = "$fHR bpm",
                            style = MaterialTheme.typography.h5,
                        )
                    }
                }
                LineChartLive(mode, value = fHR)
            }
        }
    }
}

@Preview
@Composable
fun GraphCardPreview() {
    GraphCard(1,15f,120f, 80f)
}