package com.example.samplegooglemapsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("mapentity")
data class MapSpotEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int? = null
)
