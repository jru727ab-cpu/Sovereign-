package com.civiltas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.civiltas.app.data.EarningEngine
import com.civiltas.app.data.ForecastRepository
import com.civiltas.app.data.SecretsRepository
import com.civiltas.app.ui.navigation.CiviltasNavGraph
import com.civiltas.app.ui.theme.CiviltasTheme

/**
 * Single-activity entry point for the CIVILTAS MVP.
 *
 * Repositories are created here and passed down to composables via the nav graph.
 * No DI framework is used in the MVP — manual injection keeps the build simple and fast.
 */
class MainActivity : ComponentActivity() {

    private lateinit var secretsRepository: SecretsRepository
    private lateinit var forecastRepository: ForecastRepository
    private lateinit var earningEngine: EarningEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        secretsRepository = SecretsRepository(this)
        forecastRepository = ForecastRepository(this, secretsRepository)
        earningEngine = EarningEngine(this, secretsRepository, forecastRepository)

        // Ensure confidence is up to date on launch
        forecastRepository.recalculate()

        setContent {
            CiviltasTheme {
                CiviltasNavGraph(
                    secretsRepository = secretsRepository,
                    forecastRepository = forecastRepository,
                    earningEngine = earningEngine
                )
            }
        }
    }
}
