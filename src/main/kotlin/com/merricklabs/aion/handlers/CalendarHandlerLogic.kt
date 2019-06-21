package com.merricklabs.aion.handlers

import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.HttpStatus

private val log = KotlinLogging.logger {}

class CalendarHandlerLogic : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        return when (input.httpMethod) {
            in "GET" -> handleGet()
            else -> handleGet()
        }
    }

    private fun handleGet(): APIGatewayProxyResponseEvent {
        // Todo: For now, just returning a dummy filtered calendar
        val responseBody = getBody()
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_OK
            body = responseBody
        }
    }

    private fun handlePost(): APIGatewayProxyResponseEvent {
        // Todo: Implement this
        return APIGatewayProxyResponseEvent().apply {
            statusCode = HttpStatus.SC_NOT_IMPLEMENTED
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


        val filtered = calendar.events.asSequence()
                .filter { !it.summary.value.toLowerCase().contains("microbiome") }
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