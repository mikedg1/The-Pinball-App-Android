package com.mikedg.thepinballapp.features.changelog.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mikedg.thepinballapp.data.model.ChangeLog
import com.mikedg.thepinballapp.features.home.Route


@Composable
fun ChangeLogList(
    changeLogs: List<ChangeLog>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(changeLogs) { entry ->
            ChangeLogCard(changeLog = entry, modifier = Modifier.clickable {
                entry.opdbIdReplacement?.let { navController.navigate(Route.MachineInfo(it)) }
            })
        }
        item {
            // Allows scrolling under the navigation bar
            Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangeLogListPreview() {
    val previewEntries = listOf(
        ChangeLog(
            changelogId = 1,
            opdbIdDeleted = "GrdNZ-MQo1e",
            action = "move",
            opdbIdReplacement = "GRveZ-MNE38",
            createdAt = "2018-10-19T15:06:20.000000Z",
            updatedAt = "2018-10-19T15:06:20.000000Z"
        ),
        ChangeLog(
            changelogId = 2,
            opdbIdDeleted = "AbcNZ-MQo1e",
            action = "delete",
            opdbIdReplacement = "",
            createdAt = "2019-01-10T14:23:45.000000Z",
            updatedAt = "2019-01-10T14:23:45.000000Z"
        ),
        ChangeLog(
            changelogId = 3,
            opdbIdDeleted = "XYZ12-MQa3v",
            action = "add",
            opdbIdReplacement = "NEW12-PLOY3",
            createdAt = "2022-06-15T12:10:00.000000Z",
            updatedAt = "2022-06-15T12:10:00.000000Z"
        )
    )

    ChangeLogList(
        changeLogs = previewEntries,
        modifier = Modifier.Companion.padding(10.dp),
        navController = rememberNavController()
    )
}