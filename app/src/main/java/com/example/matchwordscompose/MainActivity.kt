package com.example.matchwordscompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchwords.mvc.model.source.CapitalSource
import com.example.matchwords.mvc.model.source.RandomFilteredSource
import com.example.matchwordscompose.ui.theme.MatchWordsComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MatchWordsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MatchWordApp()
                }
            }
        }
    }
}

@Composable
fun MatchWordApp(viewModel: MatchWordsViewModel= viewModel()) {
    Column(modifier = Modifier
        .fillMaxWidth()
        ){
        Row(){
            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(2.dp, Color.Red)){
                WordList(list = viewModel.firstList,
                    selection = viewModel.first
                ) { index -> viewModel.selectFirst(index) }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Green)){
                WordList(list = viewModel.secondList,
                    selection = viewModel.second
                ) { index -> viewModel.selectSecond(index) }
            }
        }


        LazyColumn(modifier = Modifier.fillMaxWidth()){
            itemsIndexed(viewModel.selectedList){ index, pair ->
                SelectedWordPair(pair = pair,
                    status= viewModel.checkList[index],
                    gameStatus= viewModel.gameStatus,
                    map=viewModel.dict,
                    cancelSelection = {viewModel.cancelSelection(index)}
                )
            }
        }
        if(viewModel.gameStatus== Gamestatus.SELECTIONFINISHED){


                Button(onClick={viewModel.checkCorrect()}){
                    Text("Check")
                }



        }else if(viewModel.gameStatus == Gamestatus.CHECKFINISHED){
            Row() {
                TextField(
                    value =viewModel.wordCount.toString(),
                    onValueChange =  { string: String ->
                        if(string.isDigitsOnly() && string.isNotBlank()){
                            viewModel.setWordCount(string.toInt())
                        }else{

                        }

                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(onClick = { viewModel.newGame() }) {
                    Text("New Game")
                }
            }
        }
    }
}
@Composable
fun WordList(list: List<String>,
             selection: Int=-1,
             itemClicked: (Int)-> Unit ){
    LazyColumn(modifier = Modifier.padding(4.dp)){
        itemsIndexed(list){index,word->
            val offs= if(index%2==0) 0.dp else 32.dp
            val style = if(selection==index) MaterialTheme.typography.h5 else MaterialTheme.typography.body2
            val borderThickness = if(selection==index) 3.dp else 1.dp
            Text(
                text=word ,
                modifier = Modifier
                    .offset(x = offs)
                    .border(borderThickness, Color.Black)
                    .padding(borderThickness)
                    .clickable {
                        itemClicked(index)
                    }
                ,
                textAlign = TextAlign.Center,
                style = style
                )
        }
    }
}

@Composable
fun SelectedWordPair(
    pair: Pair<String,String>,
    status: CheckStatus= CheckStatus.UNCHECKED,
    gameStatus: Gamestatus,
    map: Map<String,String> ,
    cancelSelection: () -> Unit = {},
) {
    val color= when(status){
        CheckStatus.CORRECT -> Color.Green
        CheckStatus.FALSE -> Color.Red
        CheckStatus.UNCHECKED -> Color.Black
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)){

        if(gameStatus== Gamestatus.STARTED || gameStatus== Gamestatus.SELECTIONFINISHED){
        Icon(Icons.Filled.Clear, contentDescription = null, Modifier.clickable { cancelSelection()  })
    }

        Text(
            pair.first ,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(2.dp, Color.Black)
                .padding(4.dp)


            ,
            textAlign = TextAlign.Center,

        )
        Column(){
            Text(
                pair.second ,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, color)
                    .padding(4.dp)

                ,
                textAlign = TextAlign.Center
            )
            if(status== CheckStatus.FALSE){
                map[pair.first]?.let{
                    Text(text= it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color.Green)
                            .padding(4.dp)

                        ,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }




    }
}






