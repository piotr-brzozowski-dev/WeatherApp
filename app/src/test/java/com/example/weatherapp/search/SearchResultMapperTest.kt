package com.example.weatherapp.search

import com.example.weatherapp.TestCoroutineExtension
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class SearchResultMapperTest {

    private val mapper = SearchResultMapper()

    @Test
    fun `when there is no search result then mapper should return empty list`() {
        val searchResultsDto = SearchResultsDto(null)

        val result = mapper.map(searchResultsDto)

        result shouldBe emptyList()
    }

    @Test
    fun `when there is search result then mapper should return mapped`() {
        val searchResultsDto = SearchResultsDto(
            results = listOf(
                SearchResultItemDto(1, "test1", 1.0, 1.0, "PL", "Poland", "d1", "d2", "d3"),
                SearchResultItemDto(2, "test2", 2.0, 2.0, "PL", "Poland", "d1", "d2", "d3")
            )
        )

        val result = mapper.map(searchResultsDto)

        result shouldBe listOf(
            SearchResult("test1", "test1, PL, Poland, d1, d2, d3", 1.0, 1.0),
            SearchResult("test2", "test2, PL, Poland, d1, d2, d3", 2.0, 2.0),
        )
    }
}