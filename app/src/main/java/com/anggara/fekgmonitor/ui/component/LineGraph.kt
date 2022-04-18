package com.anggara.fekgmonitor.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.anggara.fekgmonitor.data.RawEcgData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


@Composable
fun MPLineChart(rawEcg: ArrayList<RawEcgData>) {
    AndroidView(
        factory = { context ->
            LineChart(context)
        },
        update = { lineChart ->
            lineChart.description.isEnabled = false
            val entries = arrayListOf<Entry>()
            rawEcg.forEachIndexed { index, d ->
                entries.add(Entry(index.toFloat(), d.ecgRaw.toFloat()))
            }
            val dataSet = LineDataSet(entries, "Ecg Raw")
            dataSet.setDrawValues(false)
            dataSet.setDrawCircles(false)
            dataSet.lineWidth = 2f

            val lineData = LineData(dataSet)
            lineChart.data = lineData
            lineChart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f, matchHeightConstraintsFirst = true)
    )
}

//@Composable
//fun LineChartLive() {
//    var thread: Thread? = null
//    var plotData = true
//    AndroidView(
//        factory = {context ->
//            LineChart(context)
//        },
//        update = { lineChart ->
//            lineChart.description.isEnabled = false
//            val data = LineData()
//            lineChart.data = data
//            val x1 = lineChart.xAxis
//            x1.setAvoidFirstLastClipping(true)
//            x1.isEnabled = true
//
//            val leftAxis = lineChart.axisLeft
//            leftAxis.setDrawGridLines(false)
//            leftAxis.axisMaximum = 10f
//            leftAxis.axisMinimum = 0f
//            leftAxis.setDrawGridLines(true)
//
//            val rightAxis = lineChart.axisRight
//            rightAxis.isEnabled = false
//
//            lineChart.axisLeft.setDrawGridLines(false)
//            lineChart.axisRight.setDrawGridLines(false)
//            lineChart.setDrawBorders(false)
//
//            feedMultiple(thread, plotData)
//        }
//
//    )
//}
//
//
//



@Preview(showBackground = true)
@Composable
fun LineGraphPreview() {
    Column {
        MPLineChart(rawEcg = arrayListOf(
            RawEcgData(0.001,1.2094677301995413),
            RawEcgData(0.002,-1.0824131997483837),
            RawEcgData(0.003,-4.353324302971458),
            RawEcgData(0.004,-5.442718031000076),
            RawEcgData(0.005,-5.895753559339987),
            RawEcgData(0.006,-6.194113690015134),
            RawEcgData(0.007,-5.76341283312437),
            RawEcgData(0.008,-6.626225821860293),
            RawEcgData(0.009000000000000001,-6.735396617238894),
            RawEcgData(0.01,-2.361310417512354)

        ))
    }
}
