package com.anggara.fekgmonitor.data

class RawEcgData(val t: Double, val ecgRaw: Double){

    constructor(tString: String, ecgRawString: String) : this(

        t = if (tString.isBlank()){
            0.0
        }else{
            tString.toDouble()
             },
        ecgRaw = if (ecgRawString.isBlank()){
            0.0
        }else{
            ecgRawString.toDouble()
        }
    )
}