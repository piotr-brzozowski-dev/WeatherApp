package com.example.weatherapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
class TestCoroutineExtension(private val dispatcher: TestDispatcher = StandardTestDispatcher()): BeforeTestExecutionCallback,
    AfterTestExecutionCallback {

    override fun beforeTestExecution(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterTestExecution(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}