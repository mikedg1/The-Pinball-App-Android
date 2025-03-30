package com.mikedg.thepinballapp.features.search.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mikedg.thepinballapp.features.home.Route
import com.mikedg.thepinballapp.features.search.TypeAheadSearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavHostController, innerPadding: PaddingValues) {
    val typeAheadSearchViewModel = hiltViewModel<TypeAheadSearchViewModel>()
    val suggestions by typeAheadSearchViewModel.typeAheadSearchResults.collectAsState()
    val query by typeAheadSearchViewModel.searchQuery.collectAsState()
    val searchResults by typeAheadSearchViewModel.searchResults.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        typeAheadSearchViewModel.scrollToTop.collect {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { newQuery ->
                typeAheadSearchViewModel.onSearchQueryChange(newQuery)
            },
            placeholder = { Text("Search...") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                coroutineScope.launch(Dispatchers.IO) {
                    if (query.isNotEmpty()) {
                        typeAheadSearchViewModel.performSearch()
                    }
                }
            }),
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            state = listState
        ) {
            if (suggestions.isNotEmpty()) {
                item {
                    Text("Suggestions", style = MaterialTheme.typography.titleMedium)
                }
                items(suggestions) { suggestion ->
                    Text(
                        text = suggestion.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Route.MachineInfo(suggestion.id))
                            }
                            .padding(vertical = 4.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (searchResults.isNotEmpty()) {
                item {
                    Text("Search Results", style = MaterialTheme.typography.titleMedium)
                }
                items(searchResults) { result ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                result.opdbId?.let { id ->
                                    navController.navigate(Route.MachineInfo(id))
                                }
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        MachineCard(machine = result)
                    }
                }
            }
            item {
                // Allows scrolling under the navigation bar
                Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
            }
        }
    }
}