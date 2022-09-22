package com.example.matchwordscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val viewModel: MatchWordsViewModel =
        viewModel(factory = MatchWordsViewModelFactory(source, count))
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row() {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .border(2.dp, Color.Red)
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
                    .fillMaxWidth()
                    .border(2.dp, Color.Green)
            ) {
                WordList(
                    list = viewModel.selectionModel.secondList.map {
                        viewModel.sourceArray[it][1]
                    },
                    selection = viewModel.selectionModel.second
                ) { index -> viewModel.selectSecond(index) }
            }
        }


        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(viewModel.selectionModel.selectedList) { index, selectedIndex ->

                SelectedWordPair(
                    pair = viewModel.sourceArray[selectedIndex.first][0] to
                            viewModel.sourceArray[selectedIndex.second][1],
                    gameStatus = viewModel.gameStatus,
                    correctMeaning = viewModel.sourceArray[selectedIndex.first][1],
                    cancelSelection = { viewModel.cancelSelection(index) }
                )
            }
        }
        if (viewModel.gameStatus == GameStatus.SELECTION_FINISHED) {
            Button(onClick = { viewModel.checkCorrect() }) {
                Text(text = stringResource(R.string.check_matching))
            }
        } else if (viewModel.gameStatus == GameStatus.GAME_FINISHED) {
            Column() {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if(viewModel.selectionModel.selectedList.isNotEmpty()){
                        var correctCount= viewModel.selectionModel.selectedList.filter {
                            it.first==it.second
                        }.size
                        Text(
                            text="$correctCount / ${viewModel.selectionModel.selectedList.size}",
                            textAlign = TextAlign.Center
                        )
                    }

                }

                Row() {
                    TextField(
                        value = viewModel.countText,
                        onValueChange = { string: String ->
                            viewModel.setWordCountText(string)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        onClick = { viewModel.newGame() },
                        enabled =  viewModel.isWordCountLegal
                    ) {
                        Text(text = stringResource(R.string.new_game))
                    }
                }
            }
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
            val style =
                if (selection == index) MaterialTheme.typography.h5 else MaterialTheme.typography.body2
            val borderThickness = if (selection == index) 3.dp else 1.dp
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = align
            ) {
                Text(
                    text = word,
                    modifier = Modifier
                        .border(borderThickness, Color.Black)
                        .padding(borderThickness)
                        .clickable {
                            itemClicked(index)
                        },
                    textAlign = TextAlign.Center,
                    style = style,
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
    cancelSelection: () -> Unit = {},
) {
    val color = if (gameStatus == GameStatus.GAME_FINISHED)
        if (pair.second == correctMeaning) Color.Green else Color.Red else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {

        if (gameStatus == GameStatus.STARTED || gameStatus == GameStatus.SELECTION_FINISHED) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = null,
                Modifier.clickable { cancelSelection() })
        }


        Text(
            text = pair.first,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(2.dp, Color.Black)
                .padding(4.dp),
            textAlign = TextAlign.Center,

            )
        Column() {
            Text(
                text = pair.second,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, color)
                    .padding(4.dp),
                textAlign = TextAlign.Center
            )

            if (gameStatus == GameStatus.GAME_FINISHED && pair.second != correctMeaning) {

                Text(
                    text = correctMeaning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.Green)
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )

            }
        }


    }
}






