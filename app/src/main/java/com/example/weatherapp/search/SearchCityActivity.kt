package com.example.weatherapp.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.error.ErrorDialogData
import com.example.weatherapp.error.ErrorDialogFragment
import com.example.weatherapp.weatherdetails.WeatherDetailsActivity
import com.example.weatherapp.weatherdetails.WeatherDetailsArgs
import com.example.weatherapp.weatherdetails.WeatherDetailsSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchCityActivity: AppCompatActivity() {

    private val searchCityViewModel by viewModels<SearchCityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchCityUi(
                searchCityViewModel.state.collectAsState(),
                searchCityViewModel::onAction
            )
        }
        onEventListener()
    }

    private fun onEventListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchCityViewModel.event.collect {
                    when (it) {
                        is SearchCityViewEvent.NavigateToWeatherDetails -> navigateToWeatherDetails(it)
                    }
                }
            }
        }
    }

    private fun navigateToWeatherDetails(searchCityViewEvent: SearchCityViewEvent.NavigateToWeatherDetails) {
        val (name, latitude, longitude) = searchCityViewEvent
        val weatherDetailsArgs = WeatherDetailsArgs(
            name,
            latitude,
            longitude,
            WeatherDetailsSource.SEARCH
        )
        startActivity(WeatherDetailsActivity.makeIntent(this, weatherDetailsArgs))
    }

    companion object {
        fun makeIntent(context: Context): Intent =
            Intent(context, SearchCityActivity::class.java)

        fun FragmentActivity.showErrorDialog(message: String?) {
            val errorData =
                ErrorDialogData(message = message.orEmpty(),
                    action = {
                        finish()
                    })
            ErrorDialogFragment.newInstance(errorData).show(supportFragmentManager, errorData.tag)
        }
    }
}