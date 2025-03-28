package com.mikedg.thepinballapp.features.changelog.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.mikedg.thepinballapp.features.changelog.ChangeLogViewModel

@Composable
fun ChangeLogScreen(changeLogViewModel: ChangeLogViewModel, navHostController: NavHostController) {
    val changelogEntries by changeLogViewModel.changeLog.collectAsState()
    ChangeLogList(
        changeLogs = changelogEntries,
        modifier = Modifier,
        navController = navHostController
    )
}
