package com.defenseunicorns.flyaware

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.defenseunicorns.flyaware.airportdetails.AirportDetailsScreen
import com.defenseunicorns.flyaware.airportlist.AirportListScreen
import com.defenseunicorns.flyaware.airportlist.AirportListViewModel


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
            val viewModel = hiltViewModel<AirportListViewModel>()

            val state by remember(viewModel) { viewModel.state }
                .collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED)

            AirportListScreen(
                modifier = modifier,
                state,
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
