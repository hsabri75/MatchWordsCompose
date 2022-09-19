package com.example.matchwordscompose

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.matchwords.mvc.model.source.SampleSource
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

enum class CheckStatus{
        UNCHECKED
        ,CORRECT
        ,FALSE
}

class MatchWordsViewModel(private val sourceArray : Array<Array<String>>) : ViewModel() {

    private val dict = sourceArray.associate {
        it[0] to it[1]
    }

    val checkList= MutableList(sourceArray.size){
        CheckStatus.UNCHECKED
    }.toMutableStateList()


    private var _isSelectionFinished by mutableStateOf(false)
    val isSelectionFinished :Boolean
    get()= _isSelectionFinished


    private val _firstList = sourceArray.map{        it[0]    }.shuffled().toMutableStateList()

    val firstList: List<String>
    get()=_firstList

    private val _secondList = sourceArray.map{        it[1]    }.shuffled().toMutableStateList()

    val secondList: List<String>
    get()=_secondList



    private val _selectedList= mutableStateListOf<Pair<String,String>>()
    val selectedList: List<Pair<String, String>>
        get() = _selectedList


    private var _first by mutableStateOf(-1)
    val first:  Int
        get() = _first
    private var _second by mutableStateOf(-1)
    val second: Int
        get() = _second




    fun selectFirst(id: Int) {
        _first = id
        checkMatch()
    }

    fun selectSecond(id: Int) {
        _second = id
        checkMatch()
    }

    private fun checkMatch() {
        if (_first != -1 && _second != -1) {
            addMatch()
        }
    }

    private fun addMatch() {

        _selectedList.add(_firstList[_first] to _secondList[_second])
        _firstList.removeAt(_first)
        _secondList.removeAt(_second)
        _isSelectionFinished = _selectedList.size == sourceArray.size
        resetSelection()
    }

    private fun resetSelection(){
        _first = -1
        _second=-1
    }

    fun checkCorrect() {
        _selectedList.forEachIndexed { index, item ->
            checkList[index]=  if (dict[item.first] == item.second) CheckStatus.CORRECT else CheckStatus.FALSE
        }
    }


}

private fun getList() = List(4) {
    "$it" to "*$it"
}