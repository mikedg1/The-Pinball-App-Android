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

    init {
        // Initialize by performing a search for a random single letter
        // Provides a fun "randomized" default
        performSearch(('a'..'z').random().toString())
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Cancel previous search job if it exists
        typeAheadSearchJob?.cancel()

        if (query.isNotBlank()) {
            // Start new search with debounce
            typeAheadSearchJob = viewModelScope.launch {
                delay(300) // Debounce timeout
                _typeAheadSearchResults.value = scoreSearchSuggestions(opdbApiService.searchTypeAhead(query), query)
            }
        } else {
            _typeAheadSearchResults.value = emptyList()
        }
    }

    fun performSearch(query: String = this.searchQuery.value) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _searchResults.value = opdbApiService.search(query)
        }
    }

    private fun scoreSearchSuggestions(suggestions: List<TypeAheadSearchResult>, query: String): List<TypeAheadSearchResult> {
        if (query.isBlank()) return suggestions

        val lowercaseQuery = query.lowercase()

        return suggestions.sortedWith(compareBy<TypeAheadSearchResult> { suggestion ->
            // First priority: exact matches (lowest number gets first priority)
            if (suggestion.name.lowercase() == lowercaseQuery) 0
            else if (suggestion.name.lowercase().startsWith(lowercaseQuery)) 1
            else 2
        }.thenByDescending { suggestion ->
            // Second priority: count of query occurrences
            suggestion.name.lowercase().windowed(
                size = lowercaseQuery.length,
                step = 1
            ).count { it == lowercaseQuery }
        }.thenBy { suggestion ->
            // Last priority: alphabetical ordering
            suggestion.name
        })
    }
}