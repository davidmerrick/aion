package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.models.CreateCalendarPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
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
    @Path("/{calendarId}")
    fun getCalendar(@PathParam("calendarId") calendarId: String): Response {
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
    @Path("/{calendarId}/apply/{filterId}")
    fun getFilteredCalendar(@PathParam("calendarId") calendarId: String,
                            @PathParam("filterId") filterId: String): Response {

        val body = logic.getFilteredCalendar(EntityId(calendarId), EntityId(filterId))
        return Response.ok(body).build()
    }
}