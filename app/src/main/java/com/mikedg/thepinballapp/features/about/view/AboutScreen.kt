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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikedg.thepinballapp.BuildConfig
import com.mikedg.thepinballapp.R

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
            text = stringResource(R.string.about_screen_about_the_pinball_app),
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
                    text = stringResource(R.string.about_screen_developer),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = stringResource(R.string.about_screen_mike_digiovanni))
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
                    text = stringResource(R.string.about_screen_powered_by_opdb),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.about_screen_opdb_thanks)
                )
                TextButton(
                    onClick = {
                        uriHandler.openUri("https://opdb.org/")
                    }
                ) {
                    Text(stringResource(R.string.about_screen_visit_opdb))
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
                    text = stringResource(R.string.about_screen_app_information),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(stringResource(R.string.about_screen_version, BuildConfig.VERSION_NAME))

                // Add Privacy Policy if you have one
                TextButton(
                    onClick = {
                        // Add your privacy policy URL here
                        // uriHandler.openUri("your-privacy-policy-url")
                    }
                ) {
                    Text(stringResource(R.string.about_screen_privacy_policy))
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
                    text = stringResource(R.string.about_screen_license),
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