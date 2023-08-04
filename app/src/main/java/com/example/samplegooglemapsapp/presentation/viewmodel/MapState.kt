package com.example.samplegooglemapsapp.presentation.viewmodel

import com.example.samplegooglemapsapp.domain.model.MapSpot
import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val mapSpots: List<MapSpot> = emptyList(),
    val isGtaSaMap: Boolean = false
)
