package com.defenseunicorns.flyaware.airportlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.defenseunicorns.flyaware.R
import com.defenseunicorns.flyaware.core.model.AirportMetar
import com.defenseunicorns.flyaware.ui.theme.FlyAwareTheme


@Preview(showBackground = true)
@Composable
fun AirportListScreenPreview() {
    FlyAwareTheme {
        //
    }
}

@Composable
fun AirportListScreen(
    modifier: Modifier = Modifier,
    state: List<AirportMetar>,
    onUiAction: (UiAction) -> Unit,
    onAirportSelected: (String) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val inputText = remember { mutableStateOf("") }

    Box {
        if (state.isEmpty() && !showDialog.value) {
            ContentEmpty(modifier)
        } else {
            AirportListContent(
                modifier = modifier,
                content = state,
                onClick = onAirportSelected
            )
        }

        if (!showDialog.value) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                onClick = { showDialog.value = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_input_add),
                    contentDescription = stringResource(R.string.airport_code_title),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(stringResource(R.string.airport_code_title)) },
                text = {
                    OutlinedTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        label = { Text(stringResource(R.string.airport_code)) },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUiAction(UiAction.Add(inputText.value))
                            showDialog.value = false
                        }
                    ) {
                        Text(stringResource(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog.value = false }
                    ) {
                        Text(stringResource(android.R.string.cancel))
                    }
                },
                properties = DialogProperties(dismissOnClickOutside = true)
            )
        }
    }
}
