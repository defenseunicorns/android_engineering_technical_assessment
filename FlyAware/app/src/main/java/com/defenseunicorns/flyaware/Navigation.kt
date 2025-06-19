package com.defenseunicorns.flyaware

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.defenseunicorns.flyaware.airportdetails.AirportDetailsScreen
import com.defenseunicorns.flyaware.airportlist.AirportListScreen


@Composable
fun ConfigureRoutes(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "airportList"
    ) {
        composable("airportList") {
            AirportListScreen(
                modifier = modifier,
                onAirportSelected = { airportId ->
                    navController.navigate("airportDetails/$airportId")
                }
            )
        }
        composable(
            route = "airportDetails/{airportId}",
            arguments = listOf(
                navArgument("airportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val airportId = backStackEntry.arguments?.getString("airportId") ?: ""
            AirportDetailsScreen(
                modifier = modifier,
                airportId = airportId
            )
        }
    }
}
