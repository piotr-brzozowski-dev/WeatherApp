package com.example.weatherapp.search

import app.cash.turbine.test
import com.example.weatherapp.TestCoroutineExtension
import com.example.weatherapp.relaxedMock
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class SearchCityViewModelTest {

    private val searchResultRepository = relaxedMock<SearchResultRepository>()
    private val viewModel by lazy {
        SearchCityViewModel(searchResultRepository)
    }

    @Test
    fun `when fetching search results then search results list should be emitted`() = runTest {
        val results = listOf<SearchResult>(relaxedMock())
        coEvery { searchResultRepository.getSearchResults("test") } returns Result.success(results)

        viewModel.state.test {
            awaitItem() shouldBe SearchCityViewState.SearchCityResultsLoaded(emptyList())
            viewModel.onAction(SearchCityViewAction.SearchCityForResults("test"))
            awaitItem() shouldBe SearchCityViewState.SearchCityResultsLoaded(results)
        }
    }

    @Test
    fun `when fetching search results with error then failed event should be emitted`() = runTest {
        coEvery { searchResultRepository.getSearchResults("test") } returns Result.failure(
            Exception(
                "test"
            )
        )

        viewModel.state.test {
            awaitItem() shouldBe SearchCityViewState.SearchCityResultsLoaded(emptyList())
            viewModel.onAction(SearchCityViewAction.SearchCityForResults("test"))
            awaitItem() shouldBe SearchCityViewState.SearchFailed("test")
        }
    }

    @Test
    fun `when navigating to weather details then navigation even with search details should be emitted`() =
        runTest {
            viewModel.event.test {
                viewModel.onAction(SearchCityViewAction.GoToWeatherDetails("test", 1.0, 1.0))

                awaitItem() shouldBe SearchCityViewEvent.NavigateToWeatherDetails("test", 1.0, 1.0)
            }
        }
}