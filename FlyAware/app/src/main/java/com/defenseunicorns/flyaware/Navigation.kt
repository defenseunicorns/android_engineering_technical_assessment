package com.defenseunicorns.flyaware

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.defenseunicorns.flyaware.airportdetails.AirportDetailsScreen
import com.defenseunicorns.flyaware.airportdetails.AirportDetailsViewModel
import com.defenseunicorns.flyaware.airportlist.AirportListScreen
import com.defenseunicorns.flyaware.airportlist.AirportListViewModel


@Composable
fun ConfigureRoutes(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AirportListRoute
    ) {
        composable<AirportListRoute> {
            val viewModel = hiltViewModel<AirportListViewModel>()

            val state by remember(viewModel) { viewModel.state }
                .collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED)

            AirportListScreen(
                modifier = modifier,
                state,
                onUiAction = { viewModel.onUiAction(it) },
                onAirportSelected = { airportId ->
                    navController.navigate(AirportDetailsRoute(airportId))
                }
            )
        }

        composable<AirportDetailsRoute> {
            val viewModel = hiltViewModel<AirportDetailsViewModel>()

            val state by remember(viewModel) { viewModel.state }
                .collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED)

            AirportDetailsScreen(
                modifier = modifier,
                airportMetar = state
            )
        }
    }
}
