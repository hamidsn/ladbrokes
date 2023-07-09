package com.example.ladbrokes.ui.race.home

import com.example.ladbrokes.data.repositories.FakeRaceRepositoryImpl
import com.example.ladbrokes.di.TestRemoteModule.provideLadbrokesApi
import com.example.ladbrokes.domain.use_case.GetCategoryFilteredRacesUseCase
import com.example.ladbrokes.domain.use_case.GetRacesUseCase
import com.example.ladbrokes.domain.use_case.GetTimeFilteredRacesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class HomeViewModelTest {

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeRaceRepositoryImpl
    private lateinit var homeViewModel: HomeViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setupViewModel() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        tasksRepository = FakeRaceRepositoryImpl(provideLadbrokesApi())

        homeViewModel = HomeViewModel(
            GetRacesUseCase(tasksRepository),
            GetTimeFilteredRacesUseCase(tasksRepository),
            GetCategoryFilteredRacesUseCase(tasksRepository)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun defaultFilterTest() {
        homeViewModel.setFilter(0, true)
        assertEquals(true, homeViewModel.filterList[2])
    }

    @Test
    fun setFilterTest() {
        homeViewModel.setFilter(0, false)
        assertEquals(false, homeViewModel.filterList[0])
    }

    @Test
    fun resetFiltersTest() {
        homeViewModel.resetFilters()
        assertEquals(false, homeViewModel.filterList[0])
        assertEquals(false, homeViewModel.filterList[1])
        assertEquals(false, homeViewModel.filterList[2])

    }

}