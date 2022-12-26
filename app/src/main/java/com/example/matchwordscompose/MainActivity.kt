package com.example.matchwordscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchwords.mvc.model.source.CapitalSource
import com.example.matchwords.mvc.model.source.ISource
import com.example.matchwordscompose.ui.theme.MatchWordsComposeTheme

import com.example.matchwordscompose.viewmodel.GameStatus
import com.example.matchwordscompose.viewmodel.MatchWordsViewModel
import com.example.matchwordscompose.viewmodel.MatchWordsViewModelFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val source = CapitalSource()
        val wordCount = 4
        setContent {
            MatchWordsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MatchWordApp(source, wordCount)
                }
            }
        }
    }
}



@Preview
@Composable
fun MatchWordsAppPreview() {
    val source = CapitalSource()
    val wordCount = 4
    MatchWordsComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {

            MatchWordApp(source, wordCount)
        }
    }
}


@Composable
fun MatchWordApp(source: ISource, count: Int) {
    val isCompact = (LocalConfiguration.current.screenWidthDp<600)
    val listState = rememberLazyListState()
    val viewModel: MatchWordsViewModel =
        viewModel(factory = MatchWordsViewModelFactory(source, count))
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        if(viewModel.gameStatus == GameStatus.GAME_FINISHED){
            Column {
                ResultScreen(viewModel)
                NewGameScreen(viewModel)
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly
        ){
            if (viewModel.gameStatus == GameStatus.STARTED) {
                QuestionScreen(viewModel,isCompact)
            }
            if(!isCompact) AnswerScreen(viewModel, isCompact,listState)
        }
        if(isCompact) AnswerScreen(viewModel, isCompact,listState)

    }
}
@Composable
fun AnswerScreen(viewModel: MatchWordsViewModel, isCompact: Boolean, listState: LazyListState){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        if (viewModel.gameStatus == GameStatus.SELECTION_FINISHED) {
            Button(onClick = { viewModel.checkCorrect() }) {
                Text(text = stringResource(R.string.check_matching))
            }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(viewModel.selectionModel.selectedList) { index, selectedIndex ->

                SelectedWordPair(
                    pair = viewModel.sourceArray[selectedIndex.first][0] to
                            viewModel.sourceArray[selectedIndex.second][1],
                    gameStatus = viewModel.gameStatus,
                    correctMeaning = viewModel.sourceArray[selectedIndex.first][1],
                    isCompact = isCompact,
                    cancelSelection = { viewModel.cancelSelection(index) }
                )
            }
        }
    }

}

@Composable
fun QuestionScreen(viewModel: MatchWordsViewModel,isCompact: Boolean){
    val widthWeight1= if (isCompact) 0.5f else 0.25f
    val widthWeight2= if (isCompact) 1f else 0.33f
    Row() {
        Box(
            modifier = Modifier
                .fillMaxWidth(widthWeight1)
                .padding(2.dp)
                .border(2.dp, MaterialTheme.colors.primary)
        ) {
            WordList(
                list = viewModel.selectionModel.firstList.map {
                    viewModel.sourceArray[it][0]
                },
                selection = viewModel.selectionModel.first
            ) { index -> viewModel.selectFirst(index) }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(widthWeight2)
                .padding(2.dp)
                .border(2.dp, MaterialTheme.colors.primary)
        ) {
            WordList(
                list = viewModel.selectionModel.secondList.map {
                    viewModel.sourceArray[it][1]
                },
                selection = viewModel.selectionModel.second
            ) { index -> viewModel.selectSecond(index) }
        }
    }
}


@Composable
fun ResultScreen(viewModel: MatchWordsViewModel){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        if(viewModel.selectionModel.selectedList.isNotEmpty()){
            var correctCount= viewModel.selectionModel.selectedList.filter {
                it.first==it.second
            }.size
            val totalQuestions=viewModel.selectionModel.selectedList.size
            val congs = if(correctCount*2>= totalQuestions) "Congratulations. " else ""
            Text(
                text="${congs} $correctCount out of $totalQuestions are correct.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun NewGameScreen(viewModel: MatchWordsViewModel){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedTextField(
            modifier= Modifier.fillMaxWidth(0.5f),
            value = viewModel.countText,
            label = {
                Text(
                    text="Number of questions (${MatchWordsViewModel.MINIMUM_QUESTION_COUNT} - ${MatchWordsViewModel.MAXIMUM_QUESTION_COUNT})")},
            onValueChange = { string: String ->
                viewModel.setWordCountText(string)
            },
            singleLine=true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Button(
            modifier= Modifier.fillMaxWidth(0.5f)  ,
            onClick = { viewModel.newGame() },
            enabled =  viewModel.isWordCountLegal
        ) {
            Text(textAlign = TextAlign.Center,
                text = stringResource(R.string.new_game))
        }
    }
}


@Composable
fun WordList(
    list: List<String>,
    selection: Int = -1,
    itemClicked: (Int) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(4.dp)) {
        itemsIndexed(list) { index, word ->
            val align = if (index % 2 == 0) Alignment.Start else Alignment.End

            val modifier = if (selection == index)
                Modifier
                    .padding(2.dp)
                    .border(2.dp, MaterialTheme.colors.primary)
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colors.primary)
                    .padding(4.dp)
                    .clickable {
                        itemClicked(index)
                    }
            else
                Modifier
                    .padding(2.dp)
                    .border(1.dp, MaterialTheme.colors.primary)
                    .padding(8.dp)
                    .clickable {
                        itemClicked(index)
                    }

            /*
            val style =
                if (selection == index) MaterialTheme.typography.h5 else MaterialTheme.typography.body2
            val borderThickness = if (selection == index) 3.dp else 1.dp
             */
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = align
            ) {
                Text(
                    text = word,
                    modifier = modifier,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
            }

        }
    }
}

@Composable
fun SelectedWordPair(
    pair: Pair<String, String>,
    gameStatus: GameStatus,
    correctMeaning: String,
    isCompact: Boolean,
    cancelSelection: () -> Unit = {},
) {

    val showCorrect = (gameStatus == GameStatus.GAME_FINISHED && pair.second != correctMeaning)

    val color = if (gameStatus == GameStatus.GAME_FINISHED)
        if (pair.second == correctMeaning) Color.Green else Color.Red else MaterialTheme.colors.secondary
    val resultIcon = if (gameStatus == GameStatus.GAME_FINISHED)
        if (pair.second == correctMeaning) "\u2714" else "\u2716" else ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (gameStatus == GameStatus.STARTED || gameStatus == GameStatus.SELECTION_FINISHED) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = null,
                Modifier.clickable { cancelSelection() },
                tint = MaterialTheme.colors.secondary
            )
        }

        Text(
            text = pair.first,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(2.dp, MaterialTheme.colors.secondary)
                .padding(4.dp),
            textAlign = TextAlign.Center,
            color=MaterialTheme.colors.secondary

            )
        Column() {
            Row{

                if(gameStatus == GameStatus.GAME_FINISHED){
                    Text(
                        text=resultIcon,
                        color = color,
                        modifier = Modifier
                            .border(2.dp, color)
                            .padding(4.dp),
                        textAlign = TextAlign.Center
                    )
                }
                    Column{
                Text(
                    text = pair.second,
                    color = color,
                    modifier = Modifier
                        //.fillMaxWidth()
                        .border(2.dp, color)
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
                        if(showCorrect && isCompact) CorrectWord(correctMeaning)
                        /*
                    if(showCorrect) {
                        if(showCorrect && isCompact)
                        CorrectWord(correctMeaning)
                    }*/
                }

                //if(showCorrect && !isCompact) {
                    if(showCorrect && !isCompact) CorrectWord(correctMeaning)
                //}

            }
        }
    }

}

@Composable
fun CorrectWord(correctMeaning: String){
    Text(
        text = correctMeaning,
        color = Color.Green,
        modifier = Modifier
            //.fillMaxWidth()
            .border(2.dp, Color.Green)
            .padding(4.dp),
        textAlign = TextAlign.Center
    )
}




