package com.example.samplegooglemapsapp.data

import android.location.Location
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

fun MapSpot.distanceTo(other: MapSpot): Float {
    val locationA = Location("point A")
    locationA.latitude = this.lat
    locationA.longitude = this.lng

    val locationB = Location("point B")
    locationB.latitude = other.lat
    locationB.longitude = other.lng

    return locationA.distanceTo(locationB)
}