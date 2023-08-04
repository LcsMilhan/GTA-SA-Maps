package com.example.samplegooglemapsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplegooglemapsapp.domain.model.MapSpot
import com.example.samplegooglemapsapp.domain.repository.MapSpotRepository
import com.example.samplegooglemapsapp.presentation.MapStyle
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapSpotRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _showClearConfirmationDialog = MutableStateFlow(false)
    val showClearConfirmationDialog = _showClearConfirmationDialog.asStateFlow()

    init {
        viewModelScope.launch {
            mapRepository.getMapSpots().collectLatest { spots ->
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
                viewModelScope.launch {
                    showClearConfirmationDialog()
                }
            }
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch {
                    mapRepository.insertMapSpot(MapSpot(
                        event.latLng.latitude,
                        event.latLng.longitude
                    ))
                }
            }
            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch {
                    mapRepository.deleteMapSpot(event.spot)
                }
            }
        }
    }

    fun clearAllSpotsConfirmed() {
        viewModelScope.launch {
            mapRepository.deleteAllMapSpots()
            _state.value = _state.value.copy(mapSpots = emptyList())
            _showClearConfirmationDialog.value = false
        }
    }
    private fun showClearConfirmationDialog() {
        viewModelScope.launch {
            _showClearConfirmationDialog.value = true
        }
    }
    fun cancelClearAllSpots() {
        _showClearConfirmationDialog.value = false
    }


}