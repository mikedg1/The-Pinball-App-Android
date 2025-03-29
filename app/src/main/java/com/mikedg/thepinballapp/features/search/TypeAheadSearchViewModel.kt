package com.mikedg.thepinballapp.features.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikedg.thepinballapp.data.model.Machine
import com.mikedg.thepinballapp.data.model.TypeAheadSearchResult
import com.mikedg.thepinballapp.data.remote.OpdbApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeAheadSearchViewModel @Inject constructor(private val opdbApiService: OpdbApiService) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _typeAheadSearchResults = MutableStateFlow<List<TypeAheadSearchResult>>(emptyList())
    val typeAheadSearchResults = _typeAheadSearchResults.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Machine>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var typeAheadSearchJob: Job? = null
    private var searchJob: Job? = null

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Cancel previous search job if it exists
        typeAheadSearchJob?.cancel()

        // Start new search with debounce
        typeAheadSearchJob = viewModelScope.launch {
            delay(300) // Debounce timeout
            _typeAheadSearchResults.value = opdbApiService.searchTypeahead(query)
        }
    }

    fun performSearch() {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _searchResults.value = opdbApiService.search(searchQuery.value)
        }
    }
}