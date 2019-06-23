package com.merricklabs.aion

import com.merricklabs.aion.storage.FilterStorage
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass

@Suppress("UNCHECKED_CAST")
open class AionIntegrationTestBase : KoinTest {

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @BeforeClass
    protected fun beforeMethod(){
        startKoin {
            modules(AionModule)
        }
        declareMock<FilterStorage>()

    }

    @AfterClass
    protected fun afterMethod() {
        stopKoin()
    }
}