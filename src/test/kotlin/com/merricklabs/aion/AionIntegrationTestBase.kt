package com.merricklabs.aion

import com.merricklabs.aion.testutil.AionTestModule
import com.merricklabs.aion.testutil.DynamoTestClient
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass

@Suppress("UNCHECKED_CAST")
open class AionIntegrationTestBase : KoinTest {

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @BeforeClass
    protected fun beforeMethod(){
        startKoin {
            modules(listOf(AionModule, AionTestModule))
        }

        initTables()
    }

    @AfterClass
    protected fun afterMethod() {
        stopKoin()
    }

    private fun initTables(){
        val dynamoClient by inject<DynamoTestClient>()
        dynamoClient.createTables()
    }
}