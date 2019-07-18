package com.merricklabs.aion.resources

import biweekly.Biweekly
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
import com.merricklabs.aion.AionIntegrationTestBase
import com.merricklabs.aion.BASE_URI
import com.merricklabs.aion.exceptions.InvalidCalendarException
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.params.FieldFilter
import com.merricklabs.aion.resources.models.AionCalendar
import com.merricklabs.aion.resources.models.AionFilter
import com.merricklabs.aion.resources.util.AionHeaders.AION_VND
import com.merricklabs.aion.storage.CalendarStorage
import com.merricklabs.aion.storage.FilterStorage
import com.merricklabs.aion.testutil.AionTestData.TEST_URL
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.http.HttpHeaders.ACCEPT
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.apache.http.HttpHeaders.LOCATION
import org.apache.http.HttpStatus
import org.apache.http.entity.ContentType
import org.koin.test.inject
import org.koin.test.mock.declareMock
import org.mockito.BDDMockito.given
import org.testng.annotations.Test

const val CALENDAR_ENDPOINT = "calendars"
const val JSON_TYPE = "application/json"

class CalendarResourceTest : AionIntegrationTestBase() {

    private val okHttpClient = OkHttpClient()
    private val mapper by inject<ObjectMapper>()
    private val calendarStorage by inject<CalendarStorage>()
    private val filterStorage by inject<FilterStorage>()

    @Test
    fun `Create a calendar`() {
        declareMock<CalendarClient> {
            val fileContent: String = com.google.common.io.Resources.getResource(com.merricklabs.aion.MEETUP_CAL_FILENAME).readText()
            org.mockito.BDDMockito.given(this.fetchCalendar(any())).willReturn(Biweekly.parse(fileContent).first())
        }

        val payload = mapOf("url" to TEST_URL)
        val body = RequestBody.create(MediaType.parse(AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT")
                .header(ACCEPT, AION_VND)
                .header(CONTENT_TYPE, AION_VND)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_CREATED

        val jsonNode = mapper.readValue(response.body()!!.string(), JsonNode::class.java)
        jsonNode.has("id") shouldBe true
        response.header(LOCATION) shouldHave contain(jsonNode.get("id").textValue())
        response.header(CONTENT_TYPE) shouldHave contain(AION_VND)
    }

    @Test
    fun `Create invalid calendar should fail`() {
        declareMock<CalendarClient> {
            given(this.validateCalendar(any())).willThrow(InvalidCalendarException("foo", "bar"))
        }

        val payload = mapOf("url" to TEST_URL)
        val body = RequestBody.create(MediaType.parse(AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT")
                .header(ACCEPT, AION_VND)
                .header(CONTENT_TYPE, AION_VND)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_BAD_REQUEST
    }

    @Test
    fun `Create calendar with unparseable url should fail`() {
        val payload = mapOf("url" to "this is garbage")
        val body = RequestBody.create(MediaType.parse(AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT")
                .header(ACCEPT, AION_VND)
                .header(CONTENT_TYPE, AION_VND)
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_BAD_REQUEST
    }

    @Test
    fun `Get a calendar`() {
        val toCreate = AionCalendar.create(TEST_URL)
        calendarStorage.saveCalendar(toCreate)
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT/${toCreate.id}")
                .header(ACCEPT, AION_VND)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_OK
    }

    @Test
    fun `Get calendar should validate accept headers`() {
        val toCreate = AionCalendar.create(TEST_URL)
        calendarStorage.saveCalendar(toCreate)
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT/${toCreate.id}")
                .header(ACCEPT, ContentType.APPLICATION_JSON.toString())
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_ACCEPTABLE
    }

    @Test
    fun `Confirm CORS filter is working`() {
        val toCreate = AionCalendar.create(TEST_URL)
        calendarStorage.saveCalendar(toCreate)
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT/${toCreate.id}")
                .header(ACCEPT, ContentType.APPLICATION_JSON.toString())
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.header(ACCESS_CONTROL_ALLOW_ORIGIN) shouldBe "*"
    }

    @Test
    fun `Should validate Accept header on create`() {
        val payload = mapOf("url" to TEST_URL)
        val body = RequestBody.create(MediaType.parse(AION_VND), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT")
                .header(ACCEPT, ContentType.APPLICATION_JSON.toString())
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_ACCEPTABLE
    }

    @Test
    fun `Should validate Content Type header on create`() {
        val payload = mapOf("url" to TEST_URL)
        val body = RequestBody.create(MediaType.parse(JSON_TYPE), mapper.writeValueAsString(payload))
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT")
                .header(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .post(body)
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE
    }

    @Test
    fun `Get invalid calendar should 404`() {
        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT/${EntityId.create()}")
                .header(ACCEPT, AION_VND)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_NOT_FOUND
    }

    @Test
    fun `Filter a calendar with include text`() {
        declareMock<CalendarClient> {
            val fileContent: String = com.google.common.io.Resources.getResource(com.merricklabs.aion.MEETUP_CAL_FILENAME).readText()
            org.mockito.BDDMockito.given(this.fetchCalendar(any())).willReturn(Biweekly.parse(fileContent).first())
        }

        val includeWord = "earthquake"
        val summaryFilter = FieldFilter(listOf(includeWord), emptyList())
        val toCreateFilter = AionFilter(EntityId.create(), summaryFilter)
        filterStorage.saveFilter(toCreateFilter)

        val toCreateCalendar = AionCalendar.create(TEST_URL)
        calendarStorage.saveCalendar(toCreateCalendar)

        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT/${toCreateCalendar.id}/apply/${toCreateFilter.id}")
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_OK
        val calendar = Biweekly.parse(response.body()!!.string()).first()
        calendar.events.size shouldBe 1
        calendar.events[0].summary.value.toLowerCase() shouldHave contain(includeWord)
    }

    @Test
    fun `Filter a calendar with exclude text`() {
        declareMock<CalendarClient> {
            val fileContent: String = com.google.common.io.Resources.getResource(com.merricklabs.aion.MEETUP_CAL_FILENAME).readText()
            org.mockito.BDDMockito.given(this.fetchCalendar(any())).willReturn(Biweekly.parse(fileContent).first())
        }

        val excludeWord = "earthquake"
        val summaryFilter = FieldFilter(emptyList(), listOf(excludeWord))
        val toCreateFilter = AionFilter(EntityId.create(), summaryFilter)
        filterStorage.saveFilter(toCreateFilter)

        val toCreateCalendar = AionCalendar.create(TEST_URL)
        calendarStorage.saveCalendar(toCreateCalendar)

        val request = Request.Builder()
                .url("$BASE_URI/$CALENDAR_ENDPOINT/${toCreateCalendar.id}/apply/${toCreateFilter.id}")
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        response.code() shouldBe HttpStatus.SC_OK
        val calendar = Biweekly.parse(response.body()!!.string()).first()
        calendar.events.size shouldBe 5
        calendar.events.asSequence().any {
            it.summary.value.toLowerCase().contains(excludeWord)
        } shouldBe false
    }
}