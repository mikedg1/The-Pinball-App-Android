package com.mikedg.thepinballapp.features.search.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.mikedg.thepinballapp.data.model.Machine
import com.mikedg.thepinballapp.features.search.MachineCardViewModel

private val IMAGE_WIDTH = 100.dp

@Composable
fun MachineCard(machine: Machine) {
    val viewModel = hiltViewModel<MachineCardViewModel>(
        key = machine.opdbId // Gets away from shared view models across all MachineCards
    )
    with(LocalDensity.current) {
        val rowHeight = viewModel.rowHeight.collectAsState()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box {
                machine.images.orEmpty().firstOrNull { it.primary ?: false }?.let { primaryImage ->
                    primaryImage.urls?.let {
                        val isDarkTheme = isSystemInDarkTheme() // Detect if we're in light theme

                        AsyncImage(
                            model = primaryImage.urls.small,
                            contentDescription = "${machine.name} image",
                            modifier = Modifier
                                .size(width = IMAGE_WIDTH * 5, height = rowHeight.value.toDp())
                                .graphicsLayer {
                                    renderEffect = BlurEffect(
                                        radiusX = 20f,
                                        radiusY = 20f,
                                        edgeTreatment = TileMode.Decal,
                                    )
                                },
                            contentScale = ContentScale.Crop,
                        )

                        Box(
                            modifier = Modifier
                                .size(width = IMAGE_WIDTH * 5, height = rowHeight.value.toDp())
                                .background(
                                    if (isDarkTheme) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
                                )
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged {
                            viewModel.updateCardSize(it)
                        }
                ) {
                    // Thumbnail image if available
                    machine.images.orEmpty().firstOrNull { it.primary ?: false }?.let { primaryImage ->
                        primaryImage.urls?.let {

                            AsyncImage(
                                model = primaryImage.urls.small,
                                contentDescription = "${machine.name} image",
                                modifier = Modifier
                                    .size(width = IMAGE_WIDTH, height = rowHeight.value.toDp()),
                                contentScale = ContentScale.Crop
                            )

                        }
                    } ?: run {
                        Box(
                            modifier = Modifier
                                .size(width = IMAGE_WIDTH, height = 0.dp)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "No Image",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Machine details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp) // Moved padding to the content column
                    ) {
                        Text(
                            text = machine.name.orEmpty(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = machine.manufacturer.name.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = machine.manufactureDate.orEmpty(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "•",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            machine.playerCount?.let { playerCount ->
                                Text(
                                    text = "${playerCount} Player${if (playerCount > 1) "s" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}