package com.example.matchwords.mvc.model.source

import kotlin.random.Random

class RandomFilteredSource(private val source: ISource, private val wordCount: Int):ISource  {

    override fun getSourceData(): Array<Array<String>> {
        val arr= source.getSourceData()
        val list= arr.toMutableList()
        val generatedList= mutableListOf<Array<String>>()
        for(i in 0 until wordCount){
            val randomNum= Random(System.currentTimeMillis()).nextInt(list.size)
            generatedList.add(list[randomNum])
            list.remove(list[randomNum])
        }
        return generatedList.toTypedArray()
    }

}