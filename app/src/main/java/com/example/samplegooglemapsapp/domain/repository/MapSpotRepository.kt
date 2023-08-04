package com.example.samplegooglemapsapp.domain.repository

import com.example.samplegooglemapsapp.domain.model.MapSpot
import kotlinx.coroutines.flow.Flow

interface MapSpotRepository {
    suspend fun insertMapSpot(spot: MapSpot)

    suspend fun deleteMapSpot(spot: MapSpot)

    fun getMapSpots(): Flow<List<MapSpot>>

}