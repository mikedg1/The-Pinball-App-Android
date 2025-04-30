package com.mikedg.thepinballapp.features.machinedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mikedg.thepinballapp.data.model.opdb.Machine
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import com.mikedg.thepinballapp.util.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineDetailViewModel(
    opdbId: String
) : ViewModel() {
    sealed class UiState {
        data object Loading : UiState()
        data class Content(val machine: Machine) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()
    
    private val opdbService = OpdbApiService() // TODO: pass dependency in, just don't want to deal with the Factories now

    init {
        loadMachineDetails(opdbId)
    }
    
    private fun loadMachineDetails(opdbId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = opdbService.fetchMachine(opdbId)) {
                is ApiResult.Success -> {
                    _uiState.value = UiState.Content(result.data)
                }
                is ApiResult.Error -> {
                    _uiState.value = UiState.Error(result.displayMessage)
                }
            }
        }
    }
    
    fun retry(opdbId: String) {
        loadMachineDetails(opdbId)
    }

    companion object {
        val OPDB_MACHINE_ID_KEY = object : CreationExtras.Key<String> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val opdbId = this[OPDB_MACHINE_ID_KEY] as String
                MachineDetailViewModel(
                    opdbId = opdbId
                )
            }
        }
    }
}