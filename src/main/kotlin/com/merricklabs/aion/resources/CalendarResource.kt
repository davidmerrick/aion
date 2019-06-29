package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject
import spark.Spark

class CalendarResource : KoinComponent {

    private val logic by inject<CalendarLogic>()
    private val mapper by inject<ObjectMapper>()

    fun defineResources() {
        Spark.get("/calendars/:id") { request, response ->
            val calendar = logic.getCalendar(EntityId(request.params("id")))
            response.type(AION_VND)
            mapper.writeValueAsString(calendar)
        }

        Spark.post("/calendars") { request, response ->
            val createPayload = mapper.readValue(request.body(), CreateCalendarPayload::class.java)
            val created = logic.createCalendar(createPayload)
            response.type(AION_VND)
            response.status(HttpStatus.SC_CREATED)
            mapper.writeValueAsString(created)
        }
    }

}