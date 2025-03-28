package com.mikedg.thepinballapp.features.machinedetail.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.mikedg.thepinballapp.data.model.Machine

@Composable
fun MachineDetailScreen(machine: Machine) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(machine.name, style = MaterialTheme.typography.headlineMedium)
        Text(machine.opdbId, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 16.dp))

        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                DetailRow("Short name", machine.shortname)
                DetailRow("Manufacturer", machine.manufacturer.name)
                DetailRow("Manufacture date", machine.manufactureDate)
                DetailRow("Type", machine.type)
                DetailRow("Display", machine.display)
                DetailRow("Player count", "${machine.playerCount} players")
                DetailRow("IPDB", "IPDB no. ${machine.ipdbId}", isLink = true) // TODO: link
                DetailRow("Keywords", machine.keywords.joinToString(", "))

                if (machine.images.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text("Images", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(8.dp))
                    Image(
                        painter = rememberAsyncImagePainter(machine.images.first().urls.medium),
                        contentDescription = "Machine Image",
                        modifier = Modifier
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Machine Group / Related Versions
        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Machine Group", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                // TODO: linkify these
                // TODO: remove place holders
                Text(
                    "Medieval Madness (${machine.manufacturer.name}, ${machine.manufactureDate.takeLast(4)})",
                    fontWeight = FontWeight.SemiBold
                )
                Text("Medieval Madness (Remake LE) (Chicago Gaming, 2015)", color = Color.Blue)
                Text("Medieval Madness (Remake Royal Edition) (Chicago Gaming, 2015)", color = Color.Blue)
                Text("Medieval Madness (Remake Special Edition) (Chicago Gaming, 2016)", color = Color.Blue)
                Text("Medieval Madness (Remake) (Chicago Gaming, 2016)", color = Color.Blue)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isLink: Boolean = false) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("$label:", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        if (isLink) {
            Text(text = value, color = Color.Blue, textDecoration = TextDecoration.Underline)
        } else {
            Text(text = value)
        }
    }
}