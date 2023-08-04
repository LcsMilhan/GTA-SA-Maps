package com.example.samplegooglemapsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MapSpotEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MapSpotDatabase : RoomDatabase() {

    abstract val dao: MapSpotDao

}