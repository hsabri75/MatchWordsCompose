package com.example.matchwords.mvc.model.source

class SampleSource: ISource {
    override fun getSourceData(): Array<Array<String>> {
        return arrayOf(
            arrayOf("1","one"),
            arrayOf("2", "two"),
            arrayOf("3", "three"),
            arrayOf("4", "four"),
            arrayOf("5", "five"),
            arrayOf("6", "six"),
            arrayOf("7", "seven"),
            arrayOf("8", "eight"),
            arrayOf("9", "nine"),
            arrayOf("10", "ten"),
        )
    }
}