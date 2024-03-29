package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.resources.logic.FilterLogic
import com.merricklabs.aion.resources.models.CreateFilterPayload
import com.merricklabs.aion.resources.util.AionHeaders.AION_VND
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

@Path("/filters")
class FilterResource : KoinComponent {

    private val logic by inject<FilterLogic>()
    private val mapper by inject<ObjectMapper>()

    @GET
    @Produces(AION_VND)
    @Path("/{$FILTER_ID}")
    fun getFilter(@PathParam(FILTER_ID) filterId: String): Response {
        val filter = logic.getFilter(EntityId(filterId))
        return Response.ok(URI(filter.id.value)).build()
    }

    @POST
    @Consumes(AION_VND)
    @Produces(AION_VND)
    fun createFilter(body: String): Response {
        val createPayload = mapper.readValue(body, CreateFilterPayload::class.java)
        val created = logic.createFilter(createPayload)
        return Response.created(URI(created.id.value))
                .entity(mapper.writeValueAsString(created))
                .build()
    }
}