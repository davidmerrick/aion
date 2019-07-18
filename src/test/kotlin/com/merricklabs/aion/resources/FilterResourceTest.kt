package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders.CONTENT_TYPE
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.BASE_URI
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.resources.models.AionFilter
import com.merricklabs.aion.resources.util.AionHeaders
import com.merricklabs.aion.resources.util.AionHeaders.AION_VND
import com.merricklabs.aion.storage.FilterStorage
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.http.HttpHeaders.ACCEPT
import org.apache.http.HttpHeaders.LOCATION
import org.apache.http.HttpStatus
import org.apache.http.entity.ContentType
import org.koin.test.inject
import org.testng.annotations.Test

const val FILTER_ENDPOINT = "filters"

class FilterResourceTest : AionIntegrationTestBase() {

    private val okHttpClient = OkHttpClient()
    private val mapper by inject<ObjectMapper>()
    private val filterStorage by inject<FilterStorage>()

    @Test
    fun `Create a filter`() {
        val payload = mapOf(
                "summary_filter" to mapOf(
                        "include" to listOf("foo")
                )
        )
        val body = RequestBody.create(MediaType.parse(AionHeaders.AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$FILTER_ENDPOINT")
                .header(ACCEPT, AION_VND)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_CREATED

        val jsonNode = mapper.readValue(response.body()!!.string(), JsonNode::class.java)
        jsonNode.has("id") shouldBe true
        response.header(LOCATION) shouldHave contain(jsonNode.get("id").textValue())
    }

    @Test
    fun `Should validate Accept header on create`() {
        val payload = mapOf(
                "summary_filter" to mapOf(
                        "include" to listOf("foo")
                )
        )
        val body = RequestBody.create(MediaType.parse(AionHeaders.AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$FILTER_ENDPOINT")
                .header(ACCEPT, ContentType.APPLICATION_JSON.toString())
                .header(CONTENT_TYPE, AION_VND)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_ACCEPTABLE
    }

    @Test
    fun `Should validate Content-Type header on create`() {
        val payload = mapOf(
                "summary_filter" to mapOf(
                        "include" to listOf("foo")
                )
        )
        val body = RequestBody.create(MediaType.parse(JSON_TYPE), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$FILTER_ENDPOINT")
                .header(ACCEPT, AION_VND)
                .header(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE
    }

    @Test
    fun `Get filter should validate accept headers`() {
        val toCreate = AionFilter(id = EntityId.create(), summaryFilter = FieldFilter(include = listOf("foo")))
        filterStorage.saveFilter(toCreate)
        val request = Request.Builder()
                .url("$BASE_URI/$FILTER_ENDPOINT/${toCreate.id}")
                .header(ACCEPT, ContentType.APPLICATION_JSON.toString())
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_ACCEPTABLE
    }

    @Test
    fun `Get invalid filter should 404`() {
        val request = Request.Builder()
                .url("$BASE_URI/$FILTER_ENDPOINT/${EntityId.create()}")
                .header(ACCEPT, AION_VND)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_FOUND
    }
}