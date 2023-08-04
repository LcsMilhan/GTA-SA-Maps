package com.example.samplegooglemapsapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MyMap(
    latLng: LatLng,
    viewModel: MapViewModel = hiltViewModel()
) {
    val latLngList = remember {
        mutableStateListOf(latLng)
    }

    val showClearConfirmationDialog by viewModel.showClearConfirmationDialog.collectAsState()
    val state by viewModel.state.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 4f)
    }

    val uiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false
        )
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
                    .padding(bottom = 30.dp)
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
                        contentDescription = "Toggle Gta Sa map",
                        tint = Color.Magenta
                    )
                }
                // Clear all spots
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(MapEvent.OnClearAllSpots)
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Clear All Spots",
                        tint = Color.Red
                    )
                }
            }
        }
    ) { paddingValues ->
        Column {
            GoogleMap(
                contentPadding = paddingValues,
                uiSettings = uiSettings,
                properties = state.properties,
                onMapLongClick = {
                    viewModel.onEvent(MapEvent.OnMapLongClick(it))
                },
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    latLngList.add(it)
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
                    latLngList.toList().forEach {
                        Marker(
                            state = MarkerState(position = it),
                            title = "My Position",
                            snippet = "Marker in current location",
                        )
                    }

                }

            }
        }

    }

}