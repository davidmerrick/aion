package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.BASE_URL
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.testutil.AionTestData.TEST_URL
import io.kotlintest.shouldBe
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.http.HttpStatus
import org.koin.test.inject
import org.testng.annotations.Test

class CalendarResourceTest : AionIntegrationTestBase() {

    private val okHttpClient by inject<OkHttpClient>()
    private val mapper by inject<ObjectMapper>()

    @Test
    fun `Create a calendar`() {
        val payload = mapOf("url" to TEST_URL)
        val body = RequestBody.create(MediaType.parse(AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URL/calendars")
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_CREATED
    }
}