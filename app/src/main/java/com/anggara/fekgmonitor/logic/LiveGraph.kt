package com.anggara.fekgmonitor.logic

import android.hardware.SensorEvent
import com.anggara.fekgmonitor.ui.home.HomeViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData


class LiveGraph(homeViewModel: HomeViewModel) {
    var thread: Thread? = null
    var plotData = true
//    val mChart = LineChart()
//
//    private fun addEntry(event: SensorEvent) {
//        val data: LineData = mChart.getData()
//        if (data != null) {
//            var set = data.getDataSetByIndex(0)
//            // set.addEntry(...); // can be called as well
//            if (set == null) {
//                set = createSet()
//                data.addDataSet(set)
//            }
//
////            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
//            data.addEntry(
//                MutableMap.MutableEntry<Any?, Any?>(set.entryCount, event.values[0] + 5),
//                0
//            )
//            data.notifyDataChanged()
//
//            // let the chart know it's data has changed
//            mChart.notifyDataSetChanged()
//
//            // limit the number of visible entries
//            mChart.setVisibleXRangeMaximum(150)
//            // mChart.setVisibleYRange(30, AxisDependency.LEFT);
//
//            // move to the latest entry
//            mChart.moveViewToX(data.entryCount)
//        }
//    }

    private fun feedMultiple() {
        if (thread != null){
            thread?.interrupt()
        }
        thread = Thread {
            while (true) {
                plotData = true
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }
}