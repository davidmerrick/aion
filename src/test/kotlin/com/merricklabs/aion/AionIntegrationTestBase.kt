package com.merricklabs.aion

import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.external.LocationResult
import com.merricklabs.aion.resources.CalendarResource
import com.merricklabs.aion.resources.FilterResource
import com.merricklabs.aion.testutil.AionTestData
import com.merricklabs.aion.testutil.AionTestModule
import com.merricklabs.aion.testutil.DynamoTestClient
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import spark.Spark

const val BASE_URL = "http://localhost:4567"

@Suppress("UNCHECKED_CAST")
open class AionIntegrationTestBase : KoinTest {

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @BeforeClass
    protected fun beforeClass() {
        startKoin {
            modules(listOf(AionModule, AionTestModule))
        }

        declareMock<GeocoderClient> {
            given(this.fetchLocation(any()))
                    .willReturn(LocationResult(AionTestData.POWELLS_LAT, AionTestData.POWELLS_LONG))
        }

        initTables()
        initResources()
    }

    @AfterClass
    protected fun afterMethod() {
        stopKoin()
    }

    private fun initTables() {
        val dynamoClient by inject<DynamoTestClient>()
        dynamoClient.createTables()
    }

    private fun initResources() {
        val calendarResource by inject<CalendarResource>()
        calendarResource.defineResources()

        val filterResource by inject<FilterResource>()
        filterResource.defineResources()
        Spark.awaitInitialization()
    }
}