package com.merricklabs.aion.handlers

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import com.amazonaws.HttpMethod.GET
import com.amazonaws.HttpMethod.POST
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.common.net.MediaType.I_CALENDAR_UTF_8
import com.merricklabs.aion.storage.AionStorage
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CalendarHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>, KoinComponent {

    private val storage by inject<AionStorage>()

    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return when (request.httpMethod) {
            in GET.toString() -> handleGet(request)
            in POST.toString() -> handlePost(request)
            else -> handleGet(request)
        }
    }

    private fun handleGet(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        val responseBody = getBody()
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_OK
            body = responseBody
            headers = mapOf(HttpHeaders.CONTENT_TYPE to I_CALENDAR_UTF_8.toString())
        }
    }

    private fun handlePost(request: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        log.info("Handling POST request")
        request.queryStringParameters["url"]?.let {
            log.info("Query string url param: $it")

            storage.saveCalendarFilter(it)
            return APIGatewayProxyResponseEvent().apply {
                statusCode = HttpStatus.SC_CREATED
            }
        }

        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_BAD_REQUEST
        }
    }

    fun getBody(): String {
        val url = "https://www.meetup.com/ScienceOnTapORWA/events/ical/"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()
        val bodyString = response.body()!!.string()
        val calendar = Biweekly.parse(bodyString).first()

        // Example filter on sold out
        val filtered = calendar.events.asSequence()
                .filter { !it.summary.value.toLowerCase().contains("sold out") }
                .toList()

        val cloned = calendar.copyWithEvents(filtered)
        return Biweekly.write(cloned).go()
    }
}

fun ICalendar.copyWithEvents(events: List<VEvent>): ICalendar {
    val newCalendar = ICalendar(this)
    newCalendar.setComponent(VEvent::class.java, null)
    events.forEach { newCalendar.addEvent(it) }
    return newCalendar
}