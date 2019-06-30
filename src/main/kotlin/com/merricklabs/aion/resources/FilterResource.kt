package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.models.CreateFilterPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.apache.http.client.HttpResponseException
import org.koin.core.KoinComponent
import org.koin.core.inject
import spark.Spark.exception
import spark.Spark.get
import spark.Spark.post

class FilterResource : KoinComponent {

    private val logic by inject<FilterLogic>()
    private val mapper by inject<ObjectMapper>()

    fun defineResources() {
        get("/filters/:id") { request, response ->
            val filter = logic.getFilter(EntityId(request.params("id")))
            response.type(AION_VND)
            mapper.writeValueAsString(filter)
        }

        post("/filters") { request, response ->
            val createPayload = mapper.readValue(request.body(), CreateFilterPayload::class.java)
            val created = logic.createFilter(createPayload)
            response.type(AION_VND)
            response.status(HttpStatus.SC_CREATED)
            response.header(HttpHeaders.LOCATION, "${request.url()}/${created.id}")
            mapper.writeValueAsString(created)
        }

        exception(HttpResponseException::class.java) { e, _, response ->
            response.status(e.statusCode)
            response.body("Resource not found")
        }
    }

}