package com.example.matchwords.mvc.model.source

interface ISource {
    fun getSourceData():Array<Array<String>>

    companion object{
        const val separator="//"
        //const val defaultFileName="data.txt"
    }
}