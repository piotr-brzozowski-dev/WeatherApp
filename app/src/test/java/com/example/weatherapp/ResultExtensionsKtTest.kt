package com.example.weatherapp

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestCoroutineExtension::class)
internal class ResultExtensionsKtTest {

    @Test
    fun `when cancelable extension is thrown then it should be rethrown`() = runTest {
        val block = suspend { throw CancellationException() }

        shouldThrow<CancellationException> {
            runSuspendCatching {
                block()
            }
        }
    }

    @Test
    fun `when non cancelable extension is thrown then it should be catched as a failure`() =
        runTest {
            val block = suspend { throw RuntimeException() }

            val result = runSuspendCatching {
                block()
            }

            result.isFailure shouldBe true
            result.exceptionOrNull() shouldBe RuntimeException()
        }

    @Test
    fun `when code is executed without exception then result should be propagated`() = runTest {
        val expectedResult = "result"
        val block = suspend { expectedResult }

        val result = runSuspendCatching {
            block()
        }

        result shouldBe Result.success(expectedResult)
    }
}