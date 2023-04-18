package com.example.weatherapp.search

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
internal class SearchResultRepositoryTest {

    private val searchCityApi = relaxedMock<SearchCityApi>()
    private val searchResultMapper = relaxedMock<SearchResultMapper>()

    private val searchResultRepository = SearchResultRepository(searchCityApi, searchResultMapper)

    @Test
    fun `when fetching search results then search results list should be returned`() = runTest {
        val searchResultsDto = relaxedMock<SearchResultsDto>()
        val expectedResults = listOf<SearchResult>(relaxedMock())
        coEvery { searchCityApi.getSearchResults("test") } returns searchResultsDto
        coEvery { searchResultMapper.map(searchResultsDto) } returns expectedResults

        val results = searchResultRepository.getSearchResults("test")

        results shouldBe Result.success(expectedResults)
    }

    @Test
    fun `when fetching search results with error then result with exception should be returned`() =
        runTest {
            coEvery { searchCityApi.getSearchResults("test") } throws Exception("test")

            val results = searchResultRepository.getSearchResults("test")

            results.isFailure shouldBe true
        }
}