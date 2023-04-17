package com.example.weatherapp.weatherdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.error.ErrorDialogData
import com.example.weatherapp.error.ErrorDialogFragment
import com.example.weatherapp.home.HomeActivity
import com.example.weatherapp.search.SearchCityActivity
import com.example.weatherapp.search.SearchCityViewEvent
import com.example.weatherapp.splash.SplashViewAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class WeatherDetailsActivity: AppCompatActivity() {

    private val weatherDetailsViewModel: WeatherDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherDetailsUi(
                weatherDetailsViewModel.state.collectAsState(),
                weatherDetailsViewModel::onAction
            )
        }
        weatherDetailsViewModel.onAction(WeatherDetailsViewAction.LoadWeatherDetails)
        onEventListener()
    }

    private fun onEventListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherDetailsViewModel.event.collect {
                    when (it) {
                        is WeatherDetailsEvent.FetchWeatherDetailsFailed -> showErrorDialog(it.message)
                        WeatherDetailsEvent.CloseScreen -> finish()
                    }
                }
            }
        }
    }

    private fun showErrorDialog(message: String?) {
        val errorData = ErrorDialogData(
            message = message.orEmpty(),
            action = {
                finish()
            })
        ErrorDialogFragment.newInstance(errorData).show(supportFragmentManager, errorData.tag)
    }

    companion object {
        internal const val ARGS = "WeatherDetailsActivityArgs"

        fun makeIntent(context: Context, weatherDetailsArgs: WeatherDetailsArgs): Intent =
            Intent(context, WeatherDetailsActivity::class.java)
                .putExtras(bundleOf(ARGS to weatherDetailsArgs))
    }
}