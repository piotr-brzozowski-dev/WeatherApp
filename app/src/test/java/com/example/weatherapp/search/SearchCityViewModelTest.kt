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

    private val searchCityApi = relaxedMock<SearchCityApi>()
    private val searchResultMapper = relaxedMock<SearchResultMapper>()
    private val viewModel by lazy {
        SearchCityViewModel(searchCityApi, searchResultMapper)
    }

    @Test
    fun `when fetching search results then search results list should be emitted`() = runTest {
        val searchResultsDto = relaxedMock<SearchResultsDto>()
        val results = listOf<SearchResult>(relaxedMock())
        coEvery { searchCityApi.getSearchResults("test") } returns searchResultsDto
        coEvery { searchResultMapper.map(searchResultsDto) } returns results
        viewModel.state.test {
            awaitItem() shouldBe SearchCityViewState.SearchCityResultsLoaded(emptyList())
            viewModel.onAction(SearchCityViewAction.SearchCityForResults("test"))
            awaitItem() shouldBe SearchCityViewState.SearchCityResultsLoaded(results)
        }
    }

    @Test
    fun `when fetching search results with error failed event should be emitted`() = runTest {
        coEvery { searchCityApi.getSearchResults("test") } throws Exception("test")
        viewModel.state.test {
            awaitItem() shouldBe SearchCityViewState.SearchCityResultsLoaded(emptyList())
            viewModel.onAction(SearchCityViewAction.SearchCityForResults("test"))
            awaitItem() shouldBe SearchCityViewState.SearchFailed("test")
        }
    }

    @Test
    fun `when navigating to weather details then navigation even with search details should be emitted`() = runTest {
        viewModel.event.test {
            viewModel.onAction(SearchCityViewAction.GoToWeatherDetails("test", 1.0, 1.0))

            awaitItem() shouldBe SearchCityViewEvent.NavigateToWeatherDetails("test", 1.0, 1.0)
        }
    }
}