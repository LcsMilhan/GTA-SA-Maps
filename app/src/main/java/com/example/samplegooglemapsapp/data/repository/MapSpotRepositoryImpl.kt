package com.example.samplegooglemapsapp.data.repository

import com.example.samplegooglemapsapp.data.MapSpotDao
import com.example.samplegooglemapsapp.data.toMapSpot
import com.example.samplegooglemapsapp.data.toMapSpotEntity
import com.example.samplegooglemapsapp.domain.model.MapSpot
import com.example.samplegooglemapsapp.domain.repository.MapSpotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MapSpotRepositoryImpl @Inject constructor(
    private val dao: MapSpotDao
) : MapSpotRepository {
    override suspend fun insertMapSpot(spot: MapSpot) {
        dao.insertMapSpot(spot.toMapSpotEntity())
    }

    override suspend fun deleteMapSpot(spot: MapSpot) {
        dao.deleteMapSpot(spot.toMapSpotEntity())
    }

    override fun getMapSpots(): Flow<List<MapSpot>> {
        return dao.getMapSpots().map { spots ->
            spots.map { it.toMapSpot() }
        }
    }
}