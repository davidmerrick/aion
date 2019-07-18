package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.resources.logic.CalendarLogic
import com.merricklabs.aion.resources.models.CreateCalendarPayload
import com.merricklabs.aion.resources.util.AionHeaders.AION_VND
import com.merricklabs.aion.resources.util.PathParams.CALENDAR_ID
import com.merricklabs.aion.resources.util.PathParams.FILTER_ID
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.URI
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path("/calendars")
class CalendarResource : KoinComponent {

    private val logic by inject<CalendarLogic>()
    private val mapper by inject<ObjectMapper>()

    @GET
    @Produces(AION_VND)
    @Path("/{$CALENDAR_ID}")
    fun getCalendar(@PathParam(CALENDAR_ID) calendarId: String): Response {
        val calendar = logic.getCalendar(EntityId(calendarId))
        return Response.ok(mapper.writeValueAsString(calendar)).build()
    }

    @POST
    @Consumes(AION_VND)
    @Produces(AION_VND)
    fun createCalendar(body: String): Response {
        val createPayload = mapper.readValue(body, CreateCalendarPayload::class.java)

        val created = logic.createCalendar(createPayload)
        return Response.created(URI(created.id.value))
                .entity(mapper.writeValueAsString(created))
                .build()
    }

    @GET
    @Produces("text/calendar")
    @Path("/{$CALENDAR_ID}/apply/{$FILTER_ID}")
    fun getFilteredCalendar(@PathParam(CALENDAR_ID) calendarId: String,
                            @PathParam(FILTER_ID) filterId: String): Response {

        val body = logic.getFilteredCalendar(EntityId(calendarId), EntityId(filterId))
        return Response.ok(body).build()
    }
}