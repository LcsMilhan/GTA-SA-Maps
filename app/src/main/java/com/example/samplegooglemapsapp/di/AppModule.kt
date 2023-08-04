package com.example.samplegooglemapsapp.di

import android.app.Application
import androidx.room.Room
import com.example.samplegooglemapsapp.common.Constants.DB_NAME
import com.example.samplegooglemapsapp.data.local.MapSpotDatabase
import com.example.samplegooglemapsapp.data.repository.MapSpotRepositoryImpl
import com.example.samplegooglemapsapp.domain.repository.MapSpotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMapSpotDatabase(app: Application): MapSpotDatabase {
        return Room.databaseBuilder(
            app,
            MapSpotDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMapSpotRepository(db: MapSpotDatabase): MapSpotRepository {
        return MapSpotRepositoryImpl(db.dao)
    }

}