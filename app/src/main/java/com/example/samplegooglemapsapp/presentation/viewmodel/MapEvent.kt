package com.example.samplegooglemapsapp.presentation.viewmodel

import com.example.samplegooglemapsapp.domain.model.MapSpot
import com.google.android.gms.maps.model.LatLng

sealed class MapEvent {
    object ToggleGtaSaMap: MapEvent()
    object OnClearAllSpots: MapEvent()
    object OnCalculateDistance: MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val spot: MapSpot): MapEvent()
}
