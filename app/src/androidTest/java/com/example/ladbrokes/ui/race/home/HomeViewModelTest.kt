package com.example.ladbrokes.ui.race.home

import com.example.ladbrokes.data.repositories.FakeRaceRepositoryImpl
import com.example.ladbrokes.di.TestRemoteModule.provideLadbrokesApi
import com.example.ladbrokes.domain.use_case.GetCategoryFilteredRacesUseCase
import com.example.ladbrokes.domain.use_case.GetRacesUseCase
import com.example.ladbrokes.domain.use_case.GetTimeFilteredRacesUseCase
import org.junit.Before

internal class HomeViewModelTest {

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeRaceRepositoryImpl
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setupViewModel() {

        tasksRepository = FakeRaceRepositoryImpl(provideLadbrokesApi())

        homeViewModel = HomeViewModel(
            GetRacesUseCase(tasksRepository),
            GetTimeFilteredRacesUseCase(tasksRepository),
            GetCategoryFilteredRacesUseCase(tasksRepository)
        )

    }
}