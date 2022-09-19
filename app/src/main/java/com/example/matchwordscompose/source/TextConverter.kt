package com.example.matchwords.mvc.model.source

import java.lang.StringBuilder

class TextConverter {

    companion object{
        const val separator="//"

        fun toArray(inputText: String): Array<Array<String>>{
            val spl = inputText.split("\n")
            val list = mutableListOf<Array<String>>()
            spl.forEach {
                if(it.contains(separator)){
                    val spl2 = it.split(separator)
                    list.add(spl2.toTypedArray())
                }
            }
            return list.toTypedArray()
        }

        fun toText(data: Array<Array<String>>):String{
            val builder= StringBuilder()
            data.forEach { builder.append("${it[0]}${separator}${it[1]}\n")}
            return builder.toString()
        }
    }
}