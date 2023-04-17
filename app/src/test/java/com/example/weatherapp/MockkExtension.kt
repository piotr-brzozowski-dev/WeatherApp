package com.example.weatherapp

import io.mockk.mockk

inline fun <reified T: Any> relaxedMock(block: T.() -> Unit = {}): T {
    return mockk(relaxed = true, block = block)
}