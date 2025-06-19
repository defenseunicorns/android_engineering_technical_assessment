package com.defenseunicorns.flyaware

import android.app.Application
import com.defenseunicorns.flyaware.data.AviationRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FlyawareApp : Application() {
    @Inject
    lateinit var repository: AviationRepository

    override fun onCreate() {
        super.onCreate()

        repository.initialize()
    }
}