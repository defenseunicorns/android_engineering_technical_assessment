package com.defenseunicorns.flyaware.data.di

import android.app.Application
import androidx.room.Room
import com.defenseunicorns.flyaware.data.local.FlyAwareDatabase
import com.defenseunicorns.flyaware.data.local.AirportDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): FlyAwareDatabase {
        return Room.databaseBuilder(
            app,
            FlyAwareDatabase::class.java,
            "flyaware_db"
        ).build()
    }

    @Provides
    fun provideAirportDao(db: FlyAwareDatabase): AirportDao = db.airportDao()
}
