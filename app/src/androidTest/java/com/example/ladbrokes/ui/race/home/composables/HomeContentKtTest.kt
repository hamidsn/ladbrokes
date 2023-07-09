package com.example.ladbrokes.ui.race.home.composables

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.example.ladbrokes.MainActivity
import com.example.ladbrokes.data.repositories.FakeRaceRepositoryImpl
import com.example.ladbrokes.di.RemoteModule
import com.example.ladbrokes.di.RepositoriesModule
import com.example.ladbrokes.di.TestRemoteModule
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.use_case.GetCategoryFilteredRacesUseCase
import com.example.ladbrokes.domain.use_case.GetRacesUseCase
import com.example.ladbrokes.domain.use_case.GetTimeFilteredRacesUseCase
import com.example.ladbrokes.ui.race.home.HomeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(RemoteModule::class, RepositoriesModule::class)
class HomeContentKtTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var tasksRepository: FakeRaceRepositoryImpl
    private lateinit var homeViewModel: HomeViewModel

    private val items = listOf<Race>(
        Race(
            categoryId = "4a2788f8-e825-4d36-9894-efd4baf1cfae",
            meetingName = "Angle Park",
            raceNumber = 8,
            seconds = 1688644140
        ), Race(
            categoryId = "4a2788f8-e825-4d36-9894-efd4baf1cfae",
            meetingName = "Race 9",
            raceNumber = 9,
            seconds = 1688643960
        ), Race(
            categoryId = "4a2788f8-e825-4d36-9894-efd4baf1cfae",
            meetingName = "Sandown Park",
            raceNumber = 11,
            seconds = 1688644320
        ), Race(
            categoryId = "4a2788f8-e825-4d36-9894-efd4basssse",
            meetingName = "Penrith",
            raceNumber = 8,
            seconds = 1688644620
        )
    )

    private val filterList: Array<Boolean> = arrayOf(true, true, true)

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()

        tasksRepository = FakeRaceRepositoryImpl(TestRemoteModule.provideLadbrokesApi())

        homeViewModel = HomeViewModel(
            GetRacesUseCase(tasksRepository),
            GetTimeFilteredRacesUseCase(tasksRepository),
            GetCategoryFilteredRacesUseCase(tasksRepository)
        )


        composeRule.activity.setContent {
            HomeContent(
                modifier = Modifier.padding(10.dp),
                onItemClicked = { },
                isLoading = false,
                characters = items,
                homeViewModel,
                filterList
            )
        }
    }

    @Test
    fun testHomeContent() {
        composeRule.onNodeWithText("Angle Park").assertIsDisplayed()
    }

}