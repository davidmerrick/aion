package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.BASE_URL
import com.merricklabs.aion.handlers.util.AionHeaders
import com.merricklabs.aion.params.EntityId
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.koin.test.inject
import org.testng.annotations.Test

const val FILTER_ENDPOINT = "filters"

class FilterResourceTest : AionIntegrationTestBase() {

    private val okHttpClient by inject<OkHttpClient>()
    private val mapper by inject<ObjectMapper>()

    @Test
    fun `Create a filter`() {
        val payload = mapOf(
                "summary_filter" to mapOf(
                        "include" to listOf("foo")
                )
        )
        val body = RequestBody.create(MediaType.parse(AionHeaders.AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URL/$FILTER_ENDPOINT")
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_CREATED

        val jsonNode = mapper.readValue(response.body()!!.string(), JsonNode::class.java)
        jsonNode.has("id") shouldBe true
        response.header(HttpHeaders.LOCATION) shouldHave contain(jsonNode.get("id").textValue())
    }

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