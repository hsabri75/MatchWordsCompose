package com.example.wordselect.io

import com.example.matchwords.mvc.model.source.ISource
import java.lang.StringBuilder

class TextConverter {
    companion object{

        fun toArray(inputText: String): Array<Array<String>>{
            val spl = inputText?.split("\n")
            val list = mutableListOf<Array<String>>()
            spl?.forEach {
                if(it.contains(ISource.separator)){
                    val spl2 = it.split(ISource.separator)
                    list.add(spl2.toTypedArray())
                }
            }
            return list.toTypedArray()
        }

        fun toText(data: Array<Array<String>>):String{
            val builder= StringBuilder()
            data.forEach { builder.append("${it[0]}${ISource.separator}${it[1]}\n")}
            return builder.toString()
        }
    }
}