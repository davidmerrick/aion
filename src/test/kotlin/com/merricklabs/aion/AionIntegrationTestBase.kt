package com.merricklabs.aion

import com.grum.geocalc.Coordinate
import com.grum.geocalc.Point
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.resources.ResourceConfigBuilder
import com.merricklabs.aion.testutil.AionTestData
import com.merricklabs.aion.testutil.AionTestModule
import com.merricklabs.aion.testutil.DynamoTestClient
import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import java.net.URI

const val BASE_URI = "http://localhost:8080"
const val MEETUP_CAL_FILENAME = "meetup.ics"
const val FACEBOOK_CAL_FILENAME = "facebook.ics"

@Suppress("UNCHECKED_CAST")
open class AionIntegrationTestBase : KoinTest {

    var server: HttpServer? = null

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @BeforeMethod
    protected fun beforeMethod() {
        startKoin {
            modules(listOf(AionModule, AionTestModule))
        }

        declareMock<GeocoderClient> {
            given(this.fetchLocation(any()))
                    .willReturn(
                            Point.at(
                                    Coordinate.fromDegrees(AionTestData.POWELLS_LAT),
                                    Coordinate.fromDegrees(AionTestData.POWELLS_LONG)
                            )
                    )
        }

        initTables()
        initResources()
    }

    @AfterMethod
    protected fun afterMethod() {
        stopKoin()
        server!!.shutdown()
    }

    private fun initTables() {
        val dynamoClient by inject<DynamoTestClient>()
        dynamoClient.createTables()
    }

    private fun initResources() {
        val resourceConfigBuilder by inject<ResourceConfigBuilder>()
        val resourceConfig = resourceConfigBuilder.build()
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig)
    }
}