package com.anggara.fekgmonitor.ui.history

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.anggara.fekgmonitor.data.RawEcgData
import com.anggara.fekgmonitor.logic.MyFileHandler

class HistoryViewModel(application: Application): AndroidViewModel(Application()) {
    private val myFileHandler = MyFileHandler()

    private val _fileList = mutableStateOf(arrayOf(String()))
    val fileList: State<Array<String>> = _fileList

    private val _rawEcgList = mutableStateOf(arrayListOf(RawEcgData("", "")))
    val rawEcgList: State<ArrayList<RawEcgData>> = _rawEcgList


    init {
        _fileList.value = myFileHandler.getFileList(application.applicationContext)
//        _rawEcgList.value.forEach { line ->
//            Log.i("History View Model", line.t)
//        }
    }

    fun setRawEcgList(newRawEcgList: ArrayList<RawEcgData>){
        _rawEcgList.value = newRawEcgList
    }

    fun onFileClick(){
        //TODO: file click function
    }


}