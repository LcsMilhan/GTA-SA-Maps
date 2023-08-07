package com.example.samplegooglemapsapp.presentation.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.samplegooglemapsapp.presentation.getCurrentLocation
import com.example.samplegooglemapsapp.presentation.viewmodel.MapViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapScreen(
    context: Context,
    viewModel: MapViewModel = hiltViewModel()
) {

    var location by remember { mutableStateOf(LatLng(0.0,0.0)) }
    var showMap by remember { mutableStateOf(false) }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        MyMap(latLng = location, viewModel = viewModel)
    } else {
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}