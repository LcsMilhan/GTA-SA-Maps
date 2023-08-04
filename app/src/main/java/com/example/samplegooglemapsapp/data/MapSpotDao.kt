package com.example.samplegooglemapsapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapSpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapSpot(spot: MapSpotEntity)

    @Delete
    fun deleteMapSpot(spot: MapSpotEntity)

    @Query("SELECT * FROM mapentity")
    fun getMapSpots(): Flow<List<MapSpotEntity>>

}