package com.mikedg.thepinballapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.mikedg.thepinballapp.ui.theme.ThePinballAppTheme

class MainActivity {
    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()

            setContent {
                ThePinballAppTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        content = { innerPadding ->
                            Text("The Pinball App")
                        })
                }
            }
        }
    }
}