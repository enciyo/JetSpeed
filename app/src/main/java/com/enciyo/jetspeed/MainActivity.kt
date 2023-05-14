package com.enciyo.jetspeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enciyo.jetspeed.ui.home.HomeRoute
import com.enciyo.jetspeed.ui.speed.SpeedRoute
import com.enciyo.jetspeed.ui.theme.JetSpeedTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetSpeedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}


@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screens.HOME) {
        composable(Screens.HOME) {
            HomeRoute(onNavigateSpeedRoute = { navController.navigate(Screens.MEASURING_SPEED) })
        }
        composable(Screens.MEASURING_SPEED) {
            SpeedRoute()
        }
    }
}


object Screens {
    const val HOME = "Home"
    const val MEASURING_SPEED = "MeasuringSpeed"
}