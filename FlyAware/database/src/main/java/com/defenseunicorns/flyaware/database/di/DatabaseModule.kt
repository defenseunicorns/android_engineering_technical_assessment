package com.defenseunicorns.flyaware.database.di

import android.content.Context
import androidx.room.Room
import com.defenseunicorns.flyaware.database.AirportDao
import com.defenseunicorns.flyaware.database.FlyawareDatabase
import com.defenseunicorns.flyaware.database.MetarDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): FlyawareDatabase = Room.databaseBuilder(
        context,
        FlyawareDatabase::class.java,
        "flyaware-database"
    )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    @Singleton
    fun provideAirportDao(database: FlyawareDatabase): AirportDao = database.airportDao()

    @Provides
    @Singleton
    fun provideMetarDao(database: FlyawareDatabase): MetarDao = database.metarDao()
}
