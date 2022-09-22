package com.example.matchwordscompose.viewmodel

import androidx.compose.runtime.*
import com.example.matchwords.mvc.model.source.RandomFilteredSource

class SelectionModel(private val count:Int) {

    private var _wordCount by  mutableStateOf(count)
    val wordCount        get()=_wordCount

    private var _firstList = mutableStateListOf<Int>()
    val firstList    get()=_firstList

    private var _secondList = mutableStateListOf<Int>()
    val secondList    get()=_secondList

    private val _selectedList= mutableStateListOf<Pair<Int,Int>>()
    val selectedList        get() = _selectedList

    private var _first by mutableStateOf(-1)
    val first        get() = _first
    private var _second by mutableStateOf(-1)
    val second        get() = _second



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
    fun newGame(){
        resetAll()
        //_gameStatus= Gamestatus.NEWGAME
        newSet()
    }

    private fun resetAll(){
        resetSelection()
        _firstList.clear()
        _secondList.clear()
        _selectedList.clear()
    }

    private fun addMatch() {
        _selectedList.add(_firstList[_first] to _secondList[_second])
        _firstList.removeAt(_first)
        _secondList.removeAt(_second)
        //heckGameStatus()
        resetSelection()
    }
    private fun resetSelection(){
        _first = -1
        _second=-1
    }


    private fun newSet(){
        _firstList = (0 until _wordCount ).shuffled().toMutableStateList()
        _secondList = (0 until _wordCount ).shuffled().toMutableStateList()
    }
    fun _cancelSelection(index: Int){
        resetSelection()
        val pair= _selectedList[index]
        _firstList.add(pair.first)
        _secondList.add(pair.second)
        _selectedList.removeAt(index)
    }
}