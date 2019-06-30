package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.BASE_URL
import com.merricklabs.aion.params.EntityId
import io.kotlintest.shouldBe
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.HttpStatus
import org.koin.test.inject
import org.testng.annotations.Test

const val FILTER_ENDPOINT = "filters"

class FilterResourceTest : AionIntegrationTestBase() {

    private val okHttpClient by inject<OkHttpClient>()
    private val mapper by inject<ObjectMapper>()

    @Test
    fun `Get invalid filter should 404`() {
        val request = Request.Builder()
                .url("$BASE_URL/$FILTER_ENDPOINT/${EntityId.create()}")
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_FOUND
    }
}