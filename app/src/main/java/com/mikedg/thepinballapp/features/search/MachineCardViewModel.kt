package com.mikedg.thepinballapp.features.search

import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val UNSET_ROW_HEIGHT = -1

@HiltViewModel
class MachineCardViewModel @Inject constructor() : ViewModel() {
    private val _rowHeight = MutableStateFlow(UNSET_ROW_HEIGHT)
    val rowHeight = _rowHeight.asStateFlow()

    fun updateCardSize(newRowSize: IntSize) {
        if (rowHeight.value == UNSET_ROW_HEIGHT) {
            // Only change once, otherwise could blow up if it causes a change
            // It doesn't as originally written though
            _rowHeight.value = newRowSize.height
        }
    }
}
