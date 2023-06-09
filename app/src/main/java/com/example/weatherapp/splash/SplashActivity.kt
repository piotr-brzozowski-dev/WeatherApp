package com.example.weatherapp.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.geo.GeoService
import com.example.weatherapp.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    @Inject
    internal lateinit var geoService: GeoService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreenView = installSplashScreen()
        splashScreenView.setKeepOnScreenCondition { true }

        viewModel.onAction(SplashViewAction.InitializeApp)
        onStateListener()
    }

    private fun onStateListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    when (it) {
                        is SplashViewState.Initialized -> onInitialized()
                        SplashViewState.Loading -> {}
                    }
                }
            }
        }
    }

    private fun onInitialized() {
        startActivity(HomeActivity.makeIntent(this))
        finish()
    }
}