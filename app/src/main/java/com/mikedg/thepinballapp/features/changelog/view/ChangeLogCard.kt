package com.mikedg.thepinballapp.features.changelog.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikedg.thepinballapp.data.model.ChangeLog


@Composable
fun ChangeLogCard(changeLog: ChangeLog, modifier: Modifier) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                text = "Changelog ID: ${changeLog.changelogId}",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Action: ${changeLog.action}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Deleted ID: ${changeLog.opdbIdDeleted}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Replacement ID: ${changeLog.opdbIdReplacement}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Created At: ${changeLog.createdAt}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Updated At: ${changeLog.updatedAt}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangelogCardPreview() {
    val previewEntry = ChangeLog(
        changelogId = 1,
        opdbIdDeleted = "GrdNZ-MQo1e",
        action = "move",
        opdbIdReplacement = "GRveZ-MNE38",
        createdAt = "2018-10-19T15:06:20.000000Z",
        updatedAt = "2018-10-19T15:06:20.000000Z"
    )
    ChangeLogCard(changeLog = previewEntry, modifier = Modifier.clickable {})
}