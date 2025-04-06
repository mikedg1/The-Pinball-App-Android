package com.mikedg.thepinballapp.features.changelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikedg.thepinballapp.data.model.ChangeLog
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLogViewModel @Inject constructor(private val opdbApiService: OpdbApiService): ViewModel() {
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
            try {
                val result = opdbApiService.fetchChangeLogs()
                _uiState.value = UiState.Content(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retry() {
        loadChangeLogs()
    }
}
// Test fail states