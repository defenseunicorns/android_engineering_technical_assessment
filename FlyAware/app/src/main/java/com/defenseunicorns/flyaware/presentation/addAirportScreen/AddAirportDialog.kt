package com.defenseunicorns.flyaware.presentation.addAirportScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.ktor.websocket.Frame

@Composable
fun AddAirportDialog(
    onDismiss: () -> Unit,
    addAirportViewModel: AddAirportViewModel = hiltViewModel()
) {
    val inputIcao = addAirportViewModel.inputIcao
    val errorMessage = addAirportViewModel.errorMessage

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Frame.Text("Add Airport") },
        text = {
            Column {
                TextField(
                    value = inputIcao,
                    onValueChange = addAirportViewModel::onInputChange,
                    label = { Text("ICAO Code") },
                    isError = errorMessage != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                addAirportViewModel.addAirport {
                    onDismiss()
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("Cancel")
            }
        }
    )
}
