package com.merricklabs.aion.resources

import com.google.common.net.MediaType
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Info
import org.apache.http.HttpHeaders.LOCATION
import org.apache.http.HttpStatus
import org.koin.core.inject
import spark.Spark
import javax.ws.rs.Path


const val CALENDARS_PATH = "/calendars"
const val GET_CALENDAR_PATH = "$CALENDARS_PATH/:id"

@OpenAPIDefinition(
        info = Info(
                title = "Aion",
                description = "iCal proxy and filter service.",
                version = "1.0"
        )
)
class CalendarResource : AionResource() {

    private val logic by inject<CalendarLogic>()

    override fun defineResources() {
        super.defineResources()

        getCalendar()

        Spark.post("/calendars") { request, response ->
            validateAcceptHeaders(request)
            validateContentTypeHeaders(request)
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

    @Operation(description = "Get a calendar")
    @Path(GET_CALENDAR_PATH)
    fun getCalendar() = Spark.get(GET_CALENDAR_PATH) { request, response ->
        validateAcceptHeaders(request)
        val calendar = logic.getCalendar(EntityId(request.params("id")))
        response.type(AION_VND)
        mapper.writeValueAsString(calendar)
    }
}