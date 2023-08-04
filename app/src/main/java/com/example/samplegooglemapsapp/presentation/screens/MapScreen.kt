package com.example.samplegooglemapsapp.presentation.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.samplegooglemapsapp.presentation.viewmodel.MapEvent
import com.example.samplegooglemapsapp.presentation.viewmodel.MapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {

    val calculatedDistance by viewModel.calculatedDistance.collectAsState()
    val state by viewModel.state.collectAsState()
    val showClearConfirmationDialog by viewModel.showClearConfirmationDialog.collectAsState()
    val selectedSpotCount by viewModel.selectedSpotCount.collectAsState()

    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }

    val camPositionState = rememberCameraPositionState {
        state.mapSpots.forEach { spot ->
            position = CameraPosition.fromLatLngZoom(LatLng(spot.lat, spot.lng), 15f)
        }
    }

    if (showClearConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.cancelClearAllSpots()
            },
            title = {
                Text("Clear All Spots")
            },
            text = {
                Text("Are you sure you want to clear all spots on the map?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearAllSpotsConfirmed()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.cancelClearAllSpots()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Toggle GTA SA Map
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(MapEvent.ToggleGtaSaMap)
                    }
                ) {
                    Icon(
                        imageVector = if (state.isGtaSaMap) {
                            Icons.Default.ToggleOff
                        } else Icons.Default.ToggleOn,
                        contentDescription = "Toggle Gta Sa map"
                    )
                }
                // Clear all spots
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(MapEvent.OnClearAllSpots)
                    }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Clear All Spots"
                    )
                }
                // Calculate distance
                CustomFab(
                    onClick = {
                        viewModel.onEvent(MapEvent.OnCalculateDistance)
                    },
                    enabled = selectedSpotCount == 2
                ) { enabled ->
                    FloatingActionButton(
                        onClick = {
                            if (enabled) {
                                viewModel.onEvent(MapEvent.OnCalculateDistance)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Calculate Distance"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column {
            GoogleMap(
                contentPadding = paddingValues,
                uiSettings = uiSettings,
                properties = state.properties,
                cameraPositionState = camPositionState,
                onMapLongClick = {
                    viewModel.onEvent(MapEvent.OnMapLongClick(it))
                }
            ) {
                state.mapSpots.forEach { spot ->
                    Marker(
                        state = rememberMarkerState(position = LatLng(spot.lat, spot.lng)),
                        title = "Spot cords: (${spot.lat}, ${spot.lng})",
                        snippet = "Long click to delete single spot",
                        onInfoWindowLongClick = {
                            viewModel.onEvent(
                                MapEvent.OnInfoWindowLongClick(spot)
                            )
                        },
                        onClick = {
                            it.showInfoWindow()
                            true
                        },
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_MAGENTA
                        )
                    )
                }

            }
            if (calculatedDistance != null) {
                Text(
                    text = "Calculated Distance: $calculatedDistance meters",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }

    }

}