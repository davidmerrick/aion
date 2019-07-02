package com.merricklabs.aion.resources

import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.BASE_URL
import io.kotlintest.shouldBe
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.HttpStatus
import org.koin.test.inject
import org.testng.annotations.Test

const val SWAGGER_ENDPOINT = "swagger"

class SwaggerResourceTest : AionIntegrationTestBase() {

    private val okHttpClient by inject<OkHttpClient>()

    @Test
    fun `Get Swagger json`() {
        val request = Request.Builder()
                .url("$BASE_URL/$SWAGGER_ENDPOINT")
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_OK
    }
}