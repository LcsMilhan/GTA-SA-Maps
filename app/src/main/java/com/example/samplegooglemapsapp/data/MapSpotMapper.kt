package com.example.samplegooglemapsapp.data

import com.example.samplegooglemapsapp.data.local.MapSpotEntity
import com.example.samplegooglemapsapp.domain.model.MapSpot

fun MapSpotEntity.toMapSpot(): MapSpot {
    return MapSpot(
        lat = lat,
        lng = lng,
        id = id
    )
}

fun MapSpot.toMapSpotEntity(): MapSpotEntity {
    return MapSpotEntity(
        lat = lat,
        lng = lng,
        id = id
    )
}