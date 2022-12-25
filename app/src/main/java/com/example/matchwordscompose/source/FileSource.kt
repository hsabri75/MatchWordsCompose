package com.example.matchwordscompose.source

import android.util.Log
import androidx.compose.ui.text.substring
import com.example.matchwords.mvc.model.source.ISource
import com.example.wordselect.io.FileClass
import com.example.wordselect.io.TextConverter
import com.example.wordselect.io.TextConverter.Companion.toArray
import java.lang.IllegalArgumentException

class FileSource(private val fileName: String): ISource {


    override fun getSourceData(): Array<Array<String>> {
        FileClass.readFromExternal(fileName)?.let{
             return toArray(it)
        }

        return arrayOf(arrayOf())

    }
}