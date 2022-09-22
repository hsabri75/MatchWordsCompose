package com.example.matchwordscompose

import android.content.pm.ActivityInfo
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import kotlin.concurrent.thread

class ApplicationTest {
    @get:Rule
    val composeAndroidRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun matchWordsApp_Rotation_ButtonInvisibleAfterRotation(){

        composeAndroidRule.onNodeWithText("New Game").performClick()

        composeAndroidRule.onNodeWithText("New Game").assertDoesNotExist()
        composeAndroidRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        composeAndroidRule.onNodeWithText("New Game").assertDoesNotExist()
    }

}