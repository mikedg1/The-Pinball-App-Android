package com.mikedg.thepinballapp.features.changelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikedg.thepinballapp.data.model.ChangeLog
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangeLogViewModel : ViewModel() {
    private val _changeLog = MutableStateFlow<List<ChangeLog>>(emptyList())
    val changeLog = _changeLog.asStateFlow()

    private val opdbService =
        OpdbApiService() // TODO: pass dependency in, just don't want to deal with the Factories now

    init {
        viewModelScope.launch {
            _changeLog.value = opdbService.fetchChangeLogs()
        }
    }
}