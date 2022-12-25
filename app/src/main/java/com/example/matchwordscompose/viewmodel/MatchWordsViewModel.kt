package com.example.matchwordscompose.viewmodel


import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matchwords.mvc.model.source.ISource
import com.example.matchwords.mvc.model.source.RandomFilteredSource



enum class GameStatus{
    STARTED,
    SELECTION_FINISHED,
    GAME_FINISHED,
}

class MatchWordsViewModel(
    private val database: ISource,
    count:Int
    ) : ViewModel() {

    val selectionModel= SelectionModel(count)

    private var _sourceArray: Array<Array<String>> = arrayOf(arrayOf()) // = RandomFilteredSource(CapitalSource(),4).getSourceData()
    val sourceArray get()=_sourceArray


    private var _gameStatus by mutableStateOf(GameStatus.GAME_FINISHED)
    val gameStatus    get()= _gameStatus

    private var _countText by mutableStateOf("$count")
    val countText get()=_countText

    private var _isWordCountLegal by mutableStateOf(true)
    val isWordCountLegal get()= _isWordCountLegal


    fun setWordCountText(text:String){
        _countText=text
        try{
            selectionModel.setWordCount( _countText.toInt())
        }catch(e: NumberFormatException){
            selectionModel.setWordCount(0)
        }
        _isWordCountLegal= (selectionModel.wordCount in 3..20)

    }

    fun selectFirst(id: Int) {
        selectionModel.selectFirst(id)
        checkGameStatus()
    }

    fun selectSecond(id: Int) {
        selectionModel.selectSecond(id)
        checkGameStatus()
    }

    fun cancelSelection(index: Int){
        selectionModel._cancelSelection(index)
        checkGameStatus()
    }


    private fun checkGameStatus(){

        if(selectionModel.selectedList.size == sourceArray.size){
            _gameStatus = GameStatus.SELECTION_FINISHED
        }else if(selectionModel.selectedList.size < sourceArray.size){
            _gameStatus = GameStatus.STARTED
        }
    }


    fun checkCorrect() {
        _gameStatus= GameStatus.GAME_FINISHED

    }

    fun newGame(){
        _sourceArray = RandomFilteredSource(database,selectionModel.wordCount).getSourceData()
        selectionModel.newGame()
        checkGameStatus()
    }


}

class MatchWordsViewModelFactory(private val database: ISource, private val count: Int): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchWordsViewModel(database,count) as T
    }
}