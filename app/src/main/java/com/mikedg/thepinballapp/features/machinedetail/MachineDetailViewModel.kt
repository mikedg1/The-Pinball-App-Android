package com.mikedg.thepinballapp.features.machinedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mikedg.thepinballapp.data.model.Machine
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineDetailViewModel(
    opdbId: String
) : ViewModel() {
    private val _machine = MutableStateFlow<Machine?>(null)
    val machine = _machine.asStateFlow()
    private val opdbService =
        OpdbApiService() // TODO: pass dependency in, just don't want to deal with the Factories now

    init {
        viewModelScope.launch {
            // TODO: this crashes if 404 or 429
            _machine.value = opdbService.fetchMachine(opdbId)
        }
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