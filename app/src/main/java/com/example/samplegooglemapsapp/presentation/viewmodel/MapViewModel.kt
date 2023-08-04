package com.example.samplegooglemapsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplegooglemapsapp.data.distanceTo
import com.example.samplegooglemapsapp.domain.model.MapSpot
import com.example.samplegooglemapsapp.domain.repository.MapSpotRepository
import com.example.samplegooglemapsapp.presentation.MapStyle
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: (FIX) -> CLEAR ALL SPOTS AND DISTANCE DON'T WORK
@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MapSpotRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state: StateFlow<MapState> = _state.asStateFlow()

    private val _calculatedDistance = MutableStateFlow<String?>(null)
    val calculatedDistance: StateFlow<String?> = _calculatedDistance.asStateFlow()

    private val _showClearConfirmationDialog = MutableStateFlow(false)
    val showClearConfirmationDialog: StateFlow<Boolean> = _showClearConfirmationDialog.asStateFlow()

    private val _selectedSpotCount = MutableStateFlow(0)
    val selectedSpotCount: StateFlow<Int> = _selectedSpotCount.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMapSpots().collectLatest { spots ->
                _state.value = state.value.copy(
                    mapSpots = spots
                )
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when(event) {
            is MapEvent.ToggleGtaSaMap -> {
                _state.value = state.value.copy(
                    properties = state.value.properties.copy(
                        mapStyleOptions = if (state.value.isGtaSaMap) {
                            null
                        } else MapStyleOptions(MapStyle.json)
                    ),
                    isGtaSaMap = !state.value.isGtaSaMap
                )
            }
            is MapEvent.OnClearAllSpots -> {
                if (_selectedSpotCount.value > 0) {
                    showClearConfirmationDialog()
                }
            }
            is MapEvent.OnCalculateDistance -> {
                if (state.value.mapSpots.size == 2) {
                    val spot1 = state.value.mapSpots[0]
                    val spot2 = state.value.mapSpots[1]
                    val distance = spot1.distanceTo(spot2)
                    val formattedDistance = String.format("%.2f", distance)
                    _calculatedDistance.value = formattedDistance
                } else _calculatedDistance.value = null
            }
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    repository.insertMapSpot(MapSpot(
                        event.latLng.latitude,
                        event.latLng.longitude
                    ))
                    _selectedSpotCount.value = _state.value.mapSpots.size
                }
            }
            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch {
                    repository.deleteMapSpot(event.spot)
                    _selectedSpotCount.value = _state.value.mapSpots.size
                }
            }
        }
    }

    fun clearAllSpotsConfirmed() {
        viewModelScope.launch {
            _state.value = _state.value.copy(mapSpots = emptyList())
            _selectedSpotCount.value = 0
        }
    }
    private fun showClearConfirmationDialog() {
        _showClearConfirmationDialog.value = true
    }

    fun cancelClearAllSpots() {
        _showClearConfirmationDialog.value = false
    }

}