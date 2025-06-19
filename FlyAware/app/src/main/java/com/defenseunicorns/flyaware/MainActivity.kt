package com.defenseunicorns.flyaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.defenseunicorns.flyaware.presentation.addAirportScreen.AddAirportDialog
import com.defenseunicorns.flyaware.presentation.airportDetails.AirportDetailsScreen
import com.defenseunicorns.flyaware.presentation.airportList.AirportsScreen
import com.defenseunicorns.flyaware.ui.theme.FlyAwareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showAddDialog by remember { mutableStateOf(false) }
            val navController = rememberNavController()
            FlyAwareTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    FloatingActionButton(onClick = { showAddDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Airport")
                    }
                }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AirportList(),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<AirportList> { backStackEntry ->
                            AirportsScreen(
                                modifier = Modifier.padding(innerPadding), navController = navController
                            )
                        }
                        composable<AirportDetails> { backStackEntry ->
                            val airportDetails = backStackEntry.toRoute<AirportDetails>()
                            AirportDetailsScreen(
                                modifier = Modifier.padding(innerPadding),
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
