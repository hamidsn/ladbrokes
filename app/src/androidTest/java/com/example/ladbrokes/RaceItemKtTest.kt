package com.example.ladbrokes

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.ui.race.home.composables.RaceItem
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class RaceItemKtTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testRaceItem() {
        val item = Race(
            categoryId = "9daef0d7-bf3c-4f50-921d-8e818c60fe61",
            meetingName = "Angle Park",
            raceNumber = 8,
            seconds = 50
        )

        composeTestRule.setContent {
            RaceItem(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                onItemClicked = {},
                textStyle = TextStyle(fontSize = 18.sp)
            )
        }

        composeTestRule
            .onNodeWithText(item.meetingName)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Race number: " + item.raceNumber)
            .assertIsDisplayed()
    }

}