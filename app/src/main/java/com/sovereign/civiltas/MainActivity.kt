package com.sovereign.civiltas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sovereign.civiltas.ui.navigation.CiviltasNavGraph
import com.sovereign.civiltas.ui.theme.CiviltasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CiviltasTheme {
                CiviltasNavGraph()
            }
        }
    }
}
