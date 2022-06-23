package com.anggara.fekgmonitor.logic

import android.content.Context
import android.util.Log
import com.anggara.fekgmonitor.R
import com.anggara.fekgmonitor.data.RawEcgData
import java.io.BufferedReader
import java.io.IOException

class MyFileHandler(private val context: Context) {
    private var rawEcgData = RawEcgData("", "")
    private val rawEcgList: ArrayList<RawEcgData> = arrayListOf(rawEcgData)

    fun getFileList(): Array<String>{
        return context.fileList()
    }

    fun readRawFile(): ArrayList<RawEcgData>{
        rawEcgList.clear()
        val inputStream = context.resources.openRawResource(R.raw.data4)
        val bufferedReader = inputStream.bufferedReader()
        read(bufferedReader)
        return rawEcgList
    }

    fun saveRawFile(fileName: String){
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {outputStream ->
            val bufferedWriter = outputStream.bufferedWriter()
            rawEcgList.forEach { line ->
                try {
                    bufferedWriter.write("${line.t},${line.ecgRaw}")
                    bufferedWriter.newLine()
                } catch (e: IOException) {
                    Log.e("My File Handler", "error: $line", e)
                }
            }
        }
    }

    fun readFile(fileName: String): ArrayList<RawEcgData>{
        rawEcgList.clear()
        val bufferedReader = context.openFileInput(fileName).bufferedReader()
        read(bufferedReader)
        return rawEcgList
    }

    fun saveFile(){
        //TODO: write file function
    }

    private fun read(bufferedReader: BufferedReader){
        var line = ""
        try {
            while (bufferedReader.readLine() != null){
                line = bufferedReader.readLine()
                val token = line.split(",")
                rawEcgData = if(token[0]== ""){
                    RawEcgData("0", token[1])
                }else if (token[1] == ""){
                    RawEcgData(token[0], "0")
                }else if (token[0]== "" && token[1] == ""){
                    RawEcgData(token[0], "0")
                }else{
                    RawEcgData(token[0], token[1])
                }
                rawEcgList.add(rawEcgData)
            }
        } catch (e: Exception) {
            Log.e("File Handler", "error: $line", e)
        }
    }
}