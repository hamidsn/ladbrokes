package com.example.ladbrokes.ui.race.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ladbrokes.data.MILLI_SECONDS
import com.example.ladbrokes.data.NUMBER_OF_DISPLAYED_RACES
import com.example.ladbrokes.data.Result
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.use_case.GetCategoryFilteredRacesUseCase
import com.example.ladbrokes.domain.use_case.GetRacesUseCase
import com.example.ladbrokes.domain.use_case.GetTimeFilteredRacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRacesUseCase: GetRacesUseCase,
    private val getTimeFilteredRacesUseCase: GetTimeFilteredRacesUseCase,
    private val getCategoryFilteredRacesUseCase: GetCategoryFilteredRacesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val state = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val filterList: Array<Boolean> = arrayOf(true, true, true)
    private var offlineList: List<Race>? = emptyList()
    private var noFilters = true
    private var fixedRateTimer: Timer = Timer()

    init {
        getRaces(getRacesUseCase())
    }

    fun setFilter(index: Int, isSelected: Boolean) {
        filterList[index] = isSelected
        noFilters = filterList[0] && filterList[1] && filterList[2]
        appLyFilters()
    }

    fun resetFilters() {
        for (i in filterList.indices) {
            filterList[i] = false
        }
        noFilters = filterList[0] && filterList[1] && filterList[2]
        appLyFilters()
    }

    private fun appLyFilters() {
        viewModelScope.launch {
            fixedRateTimer.cancel()
            getRaces(
                getCategoryFilteredRacesUseCase(
                    noFilters = noFilters,
                    filterList = filterList,
                    list = offlineList
                )
            )
        }
    }

    private fun getRaces(myList: Flow<Result<List<Race>>>) {
        viewModelScope.launch {
            myList.onEach { result ->
                when (result) {
                    is Result.Success -> {
                        getCategoryFilteredRacesUseCase(
                            noFilters = noFilters,
                            filterList = filterList,
                            list = result.data?.sortedBy { it.seconds }
                        )
                        offlineList =
                            result.data?.sortedBy { it.seconds }
                        val currentTimestamp = System.currentTimeMillis()

                        _uiState.update { homeState ->
                            homeState.copy(
                                characters = offlineList?.take(NUMBER_OF_DISPLAYED_RACES)
                                    ?: emptyList(),
                                isLoading = false,
                                filterList = filterList,
                                timeStamp = currentTimestamp
                            )
                        }

                        try {
                            val seconds = result.data?.sortedBy { it.seconds }?.get(0)?.seconds
                            val shortestTime =
                                result.data?.sortedBy { it.seconds }?.get(0)?.seconds?.minus(
                                    System.currentTimeMillis() / MILLI_SECONDS
                                )

                            withContext(Dispatchers.Default) {
                                shortestTime?.apply {
                                    val totalSeconds = this@apply + 60
                                    if (totalSeconds < 0) {
                                        getRaces(
                                            getTimeFilteredRacesUseCase(
                                                seconds ?: 0L,
                                                offlineList
                                            )
                                        )
                                    } else {
                                        fixedRateTimer = fixedRateTimer(
                                            name = "timer",
                                            initialDelay = totalSeconds * 1000L,
                                            period = totalSeconds * 1000L
                                        ) {
                                            viewModelScope.launch {
                                                getRaces(
                                                    getTimeFilteredRacesUseCase(
                                                        seconds ?: 0L,
                                                        offlineList
                                                    )
                                                )
                                            }
                                            this.cancel()
                                        }
                                    }

                                }
                            }
                        } catch (e: IndexOutOfBoundsException) {
                            _eventFlow.emit(
                                UIEvent.ShowSnackBar(
                                    result.message ?: "no race found"
                                )
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { homeState ->
                            homeState.copy(isLoading = false)
                        }

                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(
                                result.message ?: "Unknown error"
                            )
                        )
                    }

                    is Result.Loading -> {
                        _uiState.update { homeState ->
                            homeState.copy(isLoading = true)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
    }

}
