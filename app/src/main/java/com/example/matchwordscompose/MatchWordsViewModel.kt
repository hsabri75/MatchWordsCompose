package com.example.matchwordscompose

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.matchwords.mvc.model.source.CapitalSource
import com.example.matchwords.mvc.model.source.RandomFilteredSource
import com.example.matchwords.mvc.model.source.SampleSource
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

enum class CheckStatus{
        UNCHECKED
        ,CORRECT
        ,FALSE
}

enum class Gamestatus{
    STARTED,
    SELECTIONFINISHED,
    CHECKFINISHED,
    NEWGAME
}

class MatchWordsViewModel() : ViewModel() {

    private var _wordCount by  mutableStateOf(8)
    val wordCount: Int
    get()=_wordCount

    private var sourceArray: Array<Array<String>> = arrayOf(arrayOf()) // = RandomFilteredSource(CapitalSource(),4).getSourceData()

    private var _dict = mapOf<String,String>()

    val dict: Map<String,String>
    get() = _dict

    var checkList = mutableListOf<CheckStatus>()

    private var _gameStatus by mutableStateOf(Gamestatus.CHECKFINISHED)
    val gameStatus: Gamestatus
    get()= _gameStatus


    private var _firstList = mutableStateListOf<String>()


    val firstList: List<String>
    get()=_firstList

    private var _secondList = mutableStateListOf<String>()

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

    fun setWordCount(count:Int){
        _wordCount =count
    }

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
        checkGameStatus()
        resetSelection()
    }
    private fun checkGameStatus(){
        if(_selectedList.size == sourceArray.size){
            _gameStatus = Gamestatus.SELECTIONFINISHED
        }else if(_selectedList.size < sourceArray.size){
            _gameStatus = Gamestatus.STARTED
        }
    }

    private fun resetSelection(){
        _first = -1
        _second=-1
    }

    fun checkCorrect() {
        _selectedList.forEachIndexed { index, item ->
            checkList[index]=  if (_dict[item.first] == item.second) CheckStatus.CORRECT else CheckStatus.FALSE
        }
        _gameStatus= Gamestatus.CHECKFINISHED
    }

    fun newGame(){
        resetAll()
        _gameStatus= Gamestatus.NEWGAME
        newSet()
    }
    private fun resetAll(){
        resetSelection()
        _firstList.clear()
        _secondList.clear()
        _selectedList.clear()
    }
    private fun newSet(){
        sourceArray = RandomFilteredSource(CapitalSource(),_wordCount).getSourceData()
        _firstList = sourceArray.map{        it[0]    }.shuffled().toMutableStateList()
        _secondList = sourceArray.map{        it[1]    }.shuffled().toMutableStateList()
        _dict = sourceArray.associate {
            it[0] to it[1]
        }
        checkList= MutableList(sourceArray.size){
            CheckStatus.UNCHECKED
        }.toMutableStateList()
    }

    fun cancelSelection(index: Int){
        resetSelection()
        val pair= _selectedList[index]
        _firstList.add(pair.first)
        _secondList.add(pair.second)
        _selectedList.removeAt(index)
        checkGameStatus()

    }


}

