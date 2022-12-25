package com.example.matchwordscompose

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matchwords.mvc.model.source.CapitalSource
import com.example.matchwords.mvc.model.source.ISource
import com.example.matchwordscompose.source.BulmacaSource
import com.example.matchwordscompose.source.FileSource
import com.example.matchwordscompose.ui.theme.MatchWordsComposeTheme
import com.example.matchwordscompose.ui.theme.Shapes

import com.example.matchwordscompose.viewmodel.GameStatus
import com.example.matchwordscompose.viewmodel.MatchWordsViewModel
import com.example.matchwordscompose.viewmodel.MatchWordsViewModelFactory
import com.example.wordselect.io.FileClass
import com.example.wordselect.io.TextConverter


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            val wordCount = 20
            val source = FileSource("source.txt")


            setContent {
                MatchWordsComposeTheme {

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        //ErrorScreen()
                        MatchWordApp( source, count= wordCount)
                    }
                }
            }
        }
    }


    @Composable
    fun ErrorScreen() {
        Text("source.txt file not found")
    }

    @Preview
    @Composable
    fun MatchWordsAppPreview() {
        val source = BulmacaSource()
        //val wordCount = 4
        MatchWordsComposeTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {

          //      MatchWordApp(source, wordCount)
            }
        }
    }

    @Composable
    fun ScaffoldMatchWordApp(source: ISource, count: Int) {
        Scaffold(
            topBar = { MatchWordsTopBar() }
        ) {

        }

    }

    @Composable
    fun MatchWordsTopBar() {
        Text(
            text = "Match Words Game"
        )
    }


    @Composable
    fun MatchWordApp(source: ISource, count: Int) {
        val viewModel: MatchWordsViewModel =
            viewModel(factory = MatchWordsViewModelFactory(source, count))
        Column(
            //modifier = Modifier.fillMaxWidth()
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.border(2.dp, Color.Blue)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .border(2.dp, Color.Red)
                    ) {
                        if (viewModel.selectionModel.selectedList.isNotEmpty()) {
                            var correctCount = viewModel.selectionModel.selectedList.filter {
                                it.first == it.second
                            }.size
                            Spacer(Modifier.height(4.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "$correctCount / ${viewModel.selectionModel.selectedList.size}",
                                textAlign = TextAlign.Center
                            )
                        }

                    }

                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .wrapContentSize(Alignment.Center),

                        ) {
                        TextField(
                            placeholder = { "Enter number of words" },
                            label = { Text("Number of Words") },
                            value = viewModel.countText,
                            onValueChange = { string: String ->
                                viewModel.setWordCountText(string)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.secondary
                            )
                        )
                        Button(
                            onClick = { viewModel.newGame() },
                            enabled = viewModel.isWordCountLegal
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
        modifier: Modifier = Modifier,
        list: List<String>,
        selection: Int = -1,
        itemClicked: (Int) -> Unit
    ) {
        LazyColumn(modifier = modifier.padding(4.dp)) {
            itemsIndexed(list) { index, word ->
                val align = if (index % 2 == 0) Alignment.Start else Alignment.End
                val style =
                    if (selection == index) MaterialTheme.typography.h6 else MaterialTheme.typography.body1
                val borderThickness = if (selection == index) 3.dp else 1.dp

                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = align,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Surface(
                        //shape = MaterialTheme.shapes.small,

                        modifier = Modifier
                            .padding(2.dp)
                            .border(
                                borderThickness,
                                Color.Black,
                                shape = MaterialTheme.shapes.small
                            )
                            //.padding(borderThickness)
                            //.padding(4.dp)
                            .clickable {
                                itemClicked(index)
                            },
                    ) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = word,
                            textAlign = TextAlign.Center,
                            style = style,

                            )
                    }

                }


            }
        }
    }

    @Composable
    fun SelectedWordPair(
        modifier: Modifier = Modifier,
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
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly

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
                    .padding(2.dp),
                textAlign = TextAlign.Center,

                )
            Column() {
                Text(
                    text = pair.second,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, color)
                        .padding(2.dp),
                    textAlign = TextAlign.Center
                )

                if (gameStatus == GameStatus.GAME_FINISHED && pair.second != correctMeaning) {

                    Text(
                        text = correctMeaning,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color.Green)
                            .padding(2.dp),
                        textAlign = TextAlign.Center
                    )

                }
            }
        }

    }








