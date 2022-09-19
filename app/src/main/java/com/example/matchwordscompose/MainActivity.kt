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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.matchwords.mvc.model.source.CapitalSource
import com.example.matchwords.mvc.model.source.RandomFilteredSource
import com.example.matchwordscompose.ui.theme.MatchWordsComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("HSA", "$ *** on create")
        val source= RandomFilteredSource(CapitalSource(),5).getSourceData()
        setContent {
            MatchWordsComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MatchWordApp(viewModel = MatchWordsViewModel(source))
                }
            }
        }
    }
}

@Composable
fun MatchWordApp(viewModel: MatchWordsViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        ){
        Row(){
            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(2.dp, Color.Red)){
                WordList(list = viewModel.firstList
                ) { index -> viewModel.selectFirst(index) }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.Green)){
                WordList(list = viewModel.secondList
                ) { index -> viewModel.selectSecond(index) }
            }
        }

        val f= if(viewModel.first==-1) "boş" else viewModel.firstList[viewModel.first]
        val s = if(viewModel.second==-1) "boş" else viewModel.secondList[viewModel.second]
            Text(text = f )
            Text(text = s )

        LazyColumn(modifier = Modifier.fillMaxWidth()){
            itemsIndexed(viewModel.selectedList){ index, pair ->
                WordRow(pair = pair,
                    viewModel.checkList[index]
                )
            }
        }
        if(viewModel.isSelectionFinished){
            Button(onClick={viewModel.checkCorrect()}){
                Text("Check")
            }
        }
    }
}
@Composable
fun WordList(list: List<String>, itemClicked: (Int)-> Unit ){
    LazyColumn(modifier = Modifier.padding(4.dp)){
        itemsIndexed(list){index,word->
            val offs= if(index%2==0) 0.dp else 32.dp
            Text(
                text=word ,
                modifier = Modifier
                    .offset(x= offs)
                    .border(2.dp, Color.Black)
                    .padding(4.dp)
                    .clickable {
                        itemClicked(index)
                    }
                ,

                textAlign = TextAlign.Center,

                )

        }
    }
}

@Composable
fun WordRow(
    pair: Pair<String,String>,
    status: CheckStatus= CheckStatus.UNCHECKED,
    firstClicked: () -> Unit = {},
    secondClicked: () -> Unit = {}
) {
    val color= if(status== CheckStatus.FALSE) Color.Red else Color.Black
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp)){
        Text(
            pair.first ,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(2.dp, Color.Black)
                .padding(4.dp)
                .clickable {
                    firstClicked()
                }

            ,
            textAlign = TextAlign.Center,

        )

        Text(
            pair.second ,
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, color)
                .padding(4.dp)
                .clickable { secondClicked() }
            ,
            textAlign = TextAlign.Center
        )
    }
}




