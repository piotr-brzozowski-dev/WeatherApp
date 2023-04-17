package com.example.weatherapp.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.error.ErrorDialogData
import com.example.weatherapp.error.ErrorDialogFragment
import com.example.weatherapp.search.SearchCityActivity
import com.example.weatherapp.weatherdetails.WeatherDetailsActivity
import com.example.weatherapp.weatherdetails.WeatherDetailsArgs
import com.example.weatherapp.weatherdetails.WeatherDetailsSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeUi(
                homeScreenState = homeViewModel.state.collectAsState(),
                actionHandler = homeViewModel::onAction
            )
        }
        onEventListener()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.onAction(HomeScreenAction.LoadScreen)
    }

    private fun onEventListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.event.collect {
                    when (it) {
                        HomeScreenEvent.NavigateToSearch -> navigateToSearchScreen()
                        is HomeScreenEvent.NavigateToWeatherDetails -> navigateToWeatherDetails(it)
                    }
                }
            }
        }
    }

    private fun showErrorDialog() {
        val errorData = ErrorDialogData(
            message = getString(R.string.initialize_app_error),
            action = {
                finish()
            })
        ErrorDialogFragment.newInstance(errorData).show(supportFragmentManager, errorData.tag)
    }

    private fun navigateToWeatherDetails(homeScreenEvent: HomeScreenEvent.NavigateToWeatherDetails) {
        val (name, latitude, longitude) = homeScreenEvent
        val weatherDetailsArgs = WeatherDetailsArgs(
            name,
            latitude,
            longitude,
            WeatherDetailsSource.HOME
        )
        startActivity(WeatherDetailsActivity.makeIntent(this, weatherDetailsArgs))
    }

    private fun navigateToSearchScreen() {
        startActivity(SearchCityActivity.makeIntent(this))
    }

    companion object {
        fun makeIntent(context: Context): Intent =
            Intent(context, HomeActivity::class.java)
    }
}