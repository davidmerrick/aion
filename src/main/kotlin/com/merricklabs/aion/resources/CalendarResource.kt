package com.merricklabs.aion.resources

import com.google.common.net.MediaType
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
import org.apache.http.HttpHeaders.LOCATION
import org.apache.http.HttpStatus
import org.koin.core.inject
import spark.Spark


class CalendarResource : AionResource() {

    private val logic by inject<CalendarLogic>()

    override fun defineResources() {
        super.defineResources()

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
            response.header(LOCATION, "${request.url()}/${created.id}")
            mapper.writeValueAsString(created)
        }

        Spark.get("/calendars/:calendarId/apply/:filterId") { request, response ->
            val calendarId = EntityId(request.params("calendarId"))
            val filterId = EntityId(request.params("filterId"))
            response.type(MediaType.I_CALENDAR_UTF_8.toString())
            logic.getFilteredCalendar(calendarId, filterId)
        }
    }

}