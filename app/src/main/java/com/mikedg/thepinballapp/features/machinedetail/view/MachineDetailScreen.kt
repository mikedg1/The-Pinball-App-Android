package com.mikedg.thepinballapp.features.machinedetail.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.mikedg.thepinballapp.R
import com.mikedg.thepinballapp.data.model.opdb.Machine

@Composable
fun MachineDetailScreen(machine: Machine, innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))

        Text(machine.name.orEmpty(), style = MaterialTheme.typography.headlineMedium)
        Text(machine.opdbId, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 16.dp))

        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                DetailRow(stringResource(R.string.machine_detail_short_name), machine.shortname.orEmpty())
                DetailRow(stringResource(R.string.machine_detail_manufacturer), machine.manufacturer.name.orEmpty())
                DetailRow(stringResource(R.string.machine_detail_manufacture_date), machine.manufactureDate.orEmpty())
                DetailRow(stringResource(R.string.machine_detail_type), machine.type.orEmpty())
                DetailRow(stringResource(R.string.machine_detail_display), machine.display.orEmpty())
                DetailRow(
                    stringResource(R.string.machine_detail_player_count),
                    stringResource(
                        R.string.players,
                        machine.playerCount ?: stringResource(R.string.machine_detail_unknown_player_count)
                    )
                )

                val context = LocalContext.current
                DetailRow(
                    stringResource(R.string.machine_detail_ipdb),
                    stringResource(R.string.machine_detail_ipdb_no, machine.ipdbId ?: ""), isLink = true
                ) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://www.ipdb.org/machine.cgi?id=${machine.ipdbId}")
                    }
                    context.startActivity(intent)
                }
                DetailRow(
                    stringResource(R.string.machine_detail_keywords),
                    machine.keywords.orEmpty().joinToString(", ")
                )

                if (machine.images.orEmpty().isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(stringResource(R.string.machine_detail_images), style = MaterialTheme.typography.titleSmall)
                }
            }
            if (machine.images.orEmpty().isNotEmpty()) {
                machine.images.orEmpty().first().urls?.medium?.let { mediumUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(mediumUrl),
                        contentDescription = stringResource(R.string.machine_detail_machine_image),
                        modifier = Modifier
                            .height(120.dp)
                            .background(Color.LightGray)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

//        Spacer(Modifier.height(16.dp))
//        GroupInfo(machine)

        Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
    }
}

@Composable
private fun GroupInfo(machine: Machine) {

    // Machine Group / Related Versions
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.machine_detail_machine_group), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            // TODO: linkify these
            // TODO: remove place holders
            Text(
                "Medieval Madness (${machine.manufacturer.name}, ${machine.manufactureDate.orEmpty().takeLast(4)})",
                fontWeight = FontWeight.SemiBold
            )
            Text("Medieval Madness (Remake LE) (Chicago Gaming, 2015)", color = Color.Blue)
            Text("Medieval Madness (Remake Royal Edition) (Chicago Gaming, 2015)", color = Color.Blue)
            Text("Medieval Madness (Remake Special Edition) (Chicago Gaming, 2016)", color = Color.Blue)
            Text("Medieval Madness (Remake) (Chicago Gaming, 2016)", color = Color.Blue)
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onLinkClick: (() -> Unit)? = null
) {
    val modifier = if (isLink) {
        Modifier.clickable { onLinkClick?.invoke() }
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            textDecoration = if (isLink) TextDecoration.Underline else TextDecoration.None,
            color = if (isLink) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}