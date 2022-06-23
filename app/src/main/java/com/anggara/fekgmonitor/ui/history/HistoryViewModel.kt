package com.anggara.fekgmonitor.ui.history

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.anggara.fekgmonitor.data.RawEcgData
import com.anggara.fekgmonitor.logic.MyFileHandler

class HistoryViewModel(application: Application): AndroidViewModel(Application()) {
    private val myFileHandler = MyFileHandler(application.applicationContext)

    private val _fileList = mutableStateOf(arrayOf(String()))
    val fileList: State<Array<String>> = _fileList

    private val _rawEcgList = mutableStateOf(arrayListOf(RawEcgData("", "")))
    val rawEcgList: State<ArrayList<RawEcgData>> = _rawEcgList


    init {
        _rawEcgList.value = myFileHandler.readRawFile()
        myFileHandler.saveRawFile("data4raw.csv")
        _fileList.value = myFileHandler.getFileList()
    }

    fun setRawEcgList(newRawEcgList: ArrayList<RawEcgData>){
        _rawEcgList.value = newRawEcgList
    }

    fun onFileClick(fileName: String){
        Log.d("History View Model", fileName)
        _rawEcgList.value = myFileHandler.readFile(fileName)
    }


}