package com.example.weatherapp.geo

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.weatherapp.foundation.NoopActivityLifecycleCallbacks
import com.example.weatherapp.foundation.OnAppCreate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationPermissionService @Inject constructor() : OnAppCreate {

    private var topActivity: Activity? = null

    override fun onCreate(application: Application) {
        application.registerActivityLifecycleCallbacks(object : NoopActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                topActivity = p0
            }

            override fun onActivityDestroyed(p0: Activity) {
                topActivity = null
            }
        })
    }

    fun requestLocationPermission(): Flow<LocationPermissionResult> = channelFlow {
        (topActivity as? ComponentActivity)?.let {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {
                    trySendBlocking(LocationPermissionResult.SUCCESS)
                }
                else -> {
                    launchRequestPermissionLauncher(it).collect { locationPermissionResult ->
                        trySendBlocking(locationPermissionResult)
                    }
                }
            }
        }
    }

    private fun launchRequestPermissionLauncher(activity: ComponentActivity): Flow<LocationPermissionResult> =
        callbackFlow {
            val requestPermissionLauncher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    trySendBlocking(LocationPermissionResult.SUCCESS)
                } else {
                    trySendBlocking(LocationPermissionResult.RATIONALE)
                }
            }
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            awaitClose {
                requestPermissionLauncher.unregister()
            }
        }
}