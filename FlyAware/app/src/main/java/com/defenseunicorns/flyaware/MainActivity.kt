package com.defenseunicorns.flyaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.defenseunicorns.flyaware.presentation.addAirportScreen.AddAirportDialog
import com.defenseunicorns.flyaware.presentation.airportDetails.AirportDetailsScreen
import com.defenseunicorns.flyaware.presentation.airportList.AirportsScreen
import com.defenseunicorns.flyaware.presentation.airportList.AirportsViewModel
import com.defenseunicorns.flyaware.ui.theme.FlyAwareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showAddDialog by remember { mutableStateOf(false) }
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val airportsViewModel: AirportsViewModel = hiltViewModel()

            FlyAwareTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when {
                                        currentDestination?.hierarchy?.any { it.route == "AirportDetails" } == true -> {
                                            // Extract ICAO ID from the route arguments
                                            navBackStackEntry?.arguments?.getString("icaoId")
                                                ?: "Airport Details"
                                        }

                                        else -> "FlyAware"
                                    },
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                if (currentDestination?.hierarchy?.any { it.route == "AirportDetails" } == true) {
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            },
                            actions = {
                                if (currentDestination?.hierarchy?.any { it.route == "AirportList" } == true) {
                                    IconButton(onClick = { airportsViewModel.refreshData() }) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Refresh"
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showAddDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Airport"
                            )
                        }

                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AirportList(),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<AirportList> { backStackEntry ->
                            AirportsScreen(
                                navController = navController
                            )
                        }
                        composable<AirportDetails> { backStackEntry ->
                            val airportDetails = backStackEntry.toRoute<AirportDetails>()
                            AirportDetailsScreen(
                                icaoId = airportDetails.icaoId
                            )
                        }
                    }
                    if (showAddDialog) {
                        AddAirportDialog(onDismiss = { showAddDialog = false })
                    }
                }
            }
        }
    }
}
