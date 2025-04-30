package com.mikedg.thepinballapp.features.changelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikedg.thepinballapp.data.model.opdb.ChangeLog
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import com.mikedg.thepinballapp.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLogViewModel @Inject constructor(private val opdbApiService: OpdbApiService) : ViewModel() {
    sealed class UiState {
        data object Loading : UiState()
        data class Content(val changeLogs: List<ChangeLog>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadChangeLogs()
    }

    private fun loadChangeLogs() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = opdbApiService.fetchChangeLogs()) {
                is ApiResult.Success -> {
                    _uiState.value = UiState.Content(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = UiState.Error(result.displayMessage)
                }
            }
        }
    }

    fun retry() {
        loadChangeLogs()
    }
}