package com.civiltas.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.civiltas.app.data.EarningEngine
import com.civiltas.app.data.ForecastRepository
import com.civiltas.app.data.SecretsRepository
import com.civiltas.app.ui.screens.SecretDetailScreen
import com.civiltas.app.ui.screens.SecretsLibraryScreen
import com.civiltas.app.ui.screens.StoreScreen

object Routes {
    const val SECRETS_LIBRARY = "secrets_library"
    const val SECRET_DETAIL = "secret_detail/{secretId}"
    const val STORE = "store"

    fun secretDetail(secretId: String) = "secret_detail/$secretId"
}

@Composable
fun CiviltasNavGraph(
    secretsRepository: SecretsRepository,
    forecastRepository: ForecastRepository,
    earningEngine: EarningEngine,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.SECRETS_LIBRARY) {

        composable(Routes.SECRETS_LIBRARY) {
            SecretsLibraryScreen(
                secretsRepository = secretsRepository,
                forecastRepository = forecastRepository,
                earningEngine = earningEngine,
                onSecretClick = { secretId ->
                    navController.navigate(Routes.secretDetail(secretId))
                },
                onOpenStore = {
                    navController.navigate(Routes.STORE)
                }
            )
        }

        composable(Routes.SECRET_DETAIL) { backStack ->
            val secretId = backStack.arguments?.getString("secretId") ?: return@composable
            SecretDetailScreen(
                secretId = secretId,
                secretsRepository = secretsRepository,
                forecastRepository = forecastRepository,
                onBack = { navController.popBackStack() },
                onOpenStore = { navController.navigate(Routes.STORE) }
            )
        }

        composable(Routes.STORE) {
            StoreScreen()
        }
    }
}
