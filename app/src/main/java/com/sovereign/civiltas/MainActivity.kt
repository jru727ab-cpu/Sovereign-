package com.sovereign.civiltas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sovereign.civiltas.ui.theme.SovereignTheme
import com.sovereign.civiltas.ui.navigation.SovereignNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SovereignTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SovereignNavHost()
                }
            }
        }
    }
}
