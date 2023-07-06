package com.example.ladbrokes.ui.race.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ladbrokes.data.EXPIRED_DISPLAY_TIME
import com.example.ladbrokes.data.MILLI_SECONDS
import com.example.ladbrokes.data.NUMBER_OF_DISPLAYED_RACES
import com.example.ladbrokes.data.Result
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.domain.use_case.GetCategoryFilteredRacesUseCase
import com.example.ladbrokes.domain.use_case.GetRacesUseCase
import com.example.ladbrokes.domain.use_case.GetTimeFilteredRacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    private val _filterList: Array<Boolean> = arrayOf(true, true, true)
    private var offlineList: List<Race>? = emptyList()
    private var noFilters = true

    init {
        getRaces(getRacesUseCase())
    }

    fun setFilter(index: Int, isSelected: Boolean) {
        _filterList[index] = isSelected
        noFilters = _filterList[0] && _filterList[1] && _filterList[2]
        appLyFilters()
    }

    fun resetFilters() {
        for (i in _filterList.indices) {
            _filterList[i] = false
        }
        noFilters = _filterList[0] && _filterList[1] && _filterList[2]
        appLyFilters()
    }

    private fun appLyFilters() {
        viewModelScope.launch {
            getRaces(
                getCategoryFilteredRacesUseCase(
                    noFilters = noFilters,
                    filterList = _filterList,
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
                            filterList = _filterList,
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
                                filterList = _filterList,
                                timeStamp = currentTimestamp
                            )
                        }

                        try {
                            val seconds = result.data?.sortedBy { it.seconds }?.get(0)?.seconds
                            val shortestTime =
                                result.data?.sortedBy { it.seconds }?.get(0)?.seconds?.minus(
                                    System.currentTimeMillis() / MILLI_SECONDS
                                )
                            shortestTime?.apply {
                                val totalSeconds = this@apply
                                val tickSeconds = EXPIRED_DISPLAY_TIME
                                for (second in totalSeconds downTo tickSeconds) {
                                    delay(MILLI_SECONDS)
                                }
                                getRaces(getTimeFilteredRacesUseCase(seconds ?: 0L, offlineList))
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
