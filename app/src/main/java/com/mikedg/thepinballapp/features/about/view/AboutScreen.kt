package com.mikedg.thepinballapp.features.about.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikedg.thepinballapp.BuildConfig

@Composable
fun AboutScreen() {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "About The Pinball App",
            style = MaterialTheme.typography.headlineMedium
        )

        // Developer Info Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Developer",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Mike DiGiovanni")
                TextButton(
                    onClick = {
                        uriHandler.openUri("mailto:thepinballapp@mikedg.com")
                    }
                ) {
                    Text("thepinballapp@mikedg.com")
                }
            }
        }

        // OPDB Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Powered by OPDB",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Special thanks to OPDB (Open Pinball Database) for providing their API free of charge."
                )
                TextButton(
                    onClick = {
                        uriHandler.openUri("https://opdb.org/")
                    }
                ) {
                    Text("Visit OPDB")
                }
            }
        }

        // App Info Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "App Information",
                    style = MaterialTheme.typography.titleMedium
                )
                Text("Version: ${BuildConfig.VERSION_NAME}")

                // Add Privacy Policy if you have one
                TextButton(
                    onClick = {
                        // Add your privacy policy URL here
                        // uriHandler.openUri("your-privacy-policy-url")
                    }
                ) {
                    Text("Privacy Policy")
                }
            }
        }

        // License Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "License",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "© ${java.time.Year.now().value} Mike DiGiovanni. All rights reserved.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}