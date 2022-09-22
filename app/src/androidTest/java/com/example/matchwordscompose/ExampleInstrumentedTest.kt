package com.example.matchwordscompose

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.matchwords.mvc.model.source.ISource
import com.example.matchwords.mvc.model.source.SampleSource

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()



    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.matchwordscompose", appContext.packageName)
    }



    @Test
    fun matchWordsApp_StartScreen_NewGameButtonExists(){
        composeTestRule.setContent {
            MaterialTheme {
                MatchWordApp(TestSource(), 3)
            }
        }
        composeTestRule.onNodeWithText("New Game").assertExists()
    }
    @Test
    fun matchWordsApp_NewGameButton_WordsListVisible(){
        composeTestRule.setContent {
            MaterialTheme {
                MatchWordApp(TestSource(), 3)
            }
        }
        composeTestRule.onNodeWithText("New Game").performClick()
        composeTestRule.onNodeWithText("Ireland").assertExists()
        composeTestRule.onNodeWithText("Dublin").assertExists()
    }



    @Test
    fun matchWordsAppTest_Selection_FirstListClickable_SecondNot(){
        composeTestRule.setContent {
            MaterialTheme{
                MatchWordApp(TestSource(),3)
            }
        }
        composeTestRule.onNodeWithText("New Game").performClick()
        composeTestRule.onNodeWithText("Ireland").performClick()
        composeTestRule.onNodeWithText("Dublin").assertHasClickAction()
        composeTestRule.onNodeWithText("Dublin").performClick()
        composeTestRule.onNodeWithText("Dublin").assertHasNoClickAction()
    }

    @Test
    fun matchWordsAppTest_SelectionFinished_CheckButtonExists(){
        composeTestRule.setContent {
            MaterialTheme{
                MatchWordApp(TestSource(),3)
            }
        }
        composeTestRule.onNodeWithText("New Game").performClick()
        composeTestRule.onNodeWithText("Iran").performClick()
        composeTestRule.onNodeWithText("Tehran").performClick()
        composeTestRule.onNodeWithText("Iraq").performClick()
        composeTestRule.onNodeWithText("Baghdad").performClick()
        composeTestRule.onNodeWithText("Ireland").performClick()
        composeTestRule.onNodeWithText("Dublin").performClick()
        composeTestRule.onNodeWithText("Check").assertExists()

    }

    @Test
    fun matchWordsAppTest_CheckButton_NewGameButtonVisible(){
        composeTestRule.setContent {
            MaterialTheme{
                MatchWordApp(TestSource(),3)
            }
        }
        composeTestRule.onNodeWithText("New Game").performClick()
        composeTestRule.onNodeWithText("Iran").performClick()
        composeTestRule.onNodeWithText("Tehran").performClick()
        composeTestRule.onNodeWithText("Iraq").performClick()
        composeTestRule.onNodeWithText("Baghdad").performClick()
        composeTestRule.onNodeWithText("Ireland").performClick()
        composeTestRule.onNodeWithText("Dublin").performClick()
        composeTestRule.onNodeWithText("Check").performClick()
        composeTestRule.onNodeWithText("New Game").assertExists()


    }
}

class TestSource : ISource {
    override fun getSourceData(): Array<Array<String>> {
        return arrayOf(
            arrayOf("Iran", "Tehran"),
            arrayOf("Iraq", "Baghdad"),
            arrayOf("Ireland", "Dublin"),
        )
    }
}