package com.anggara.fekgmonitor.logic

import android.content.Context

class MyFileHandler {

    fun getFileList(context: Context): Array<String>{
        return context.fileList()
    }

    fun readFile(){
        //TODO: read file function
    }

    fun saveFile(){
        //TODO: write file function
    }
}