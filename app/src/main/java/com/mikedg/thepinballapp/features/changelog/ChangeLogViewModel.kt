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
    private val _changeLog = MutableStateFlow<List<ChangeLog>>(emptyList())
    val changeLog = _changeLog.asStateFlow()

    init {
        viewModelScope.launch {
            _changeLog.value = opdbApiService.fetchChangeLogs()
        }
    }
}