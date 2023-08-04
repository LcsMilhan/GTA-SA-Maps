package com.example.samplegooglemapsapp.presentation.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.samplegooglemapsapp.presentation.getCurrentLocation
import com.google.android.gms.maps.model.LatLng

@Composable
fun MapScreen(
    context: Context
) {
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var showMap by remember { mutableStateOf(false) }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (!showMap) {
        MyMap(latLng = location)
    }
}