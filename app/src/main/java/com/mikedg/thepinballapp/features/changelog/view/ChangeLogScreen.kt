package com.mikedg.thepinballapp.features.changelog.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mikedg.thepinballapp.features.changelog.ChangeLogViewModel
import com.mikedg.thepinballapp.view.ErrorState

@Composable
fun ChangeLogScreen(
    changeLogViewModel: ChangeLogViewModel,
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {
    val uiState by changeLogViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is ChangeLogViewModel.UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).padding(innerPadding)
                )
            }
            is ChangeLogViewModel.UiState.Content -> {
                ChangeLogList(
                    changeLogs = (uiState as ChangeLogViewModel.UiState.Content).changeLogs,
                    modifier = Modifier,
                    navController = navHostController,
                    innerPadding = innerPadding
                )
            }
            is ChangeLogViewModel.UiState.Error -> {
                ErrorState(
                    message = (uiState as ChangeLogViewModel.UiState.Error).message,
                    onRetry = { changeLogViewModel.retry() },
                    modifier = Modifier.align(Alignment.Center).padding(innerPadding)
                )
            }
        }
    }
}
