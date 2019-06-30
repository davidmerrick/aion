package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.models.CreateFilterPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
import org.apache.http.HttpStatus
import org.koin.core.KoinComponent
import org.koin.core.inject
import spark.Spark

class FilterResource : KoinComponent {

    private val logic by inject<FilterLogic>()
    private val mapper by inject<ObjectMapper>()

    fun defineResources() {
        Spark.get("/filters/:id") { request, response ->
            val filter = logic.getFilter(EntityId(request.params("id")))
            response.type(AION_VND)
            mapper.writeValueAsString(filter)
        }

        Spark.post("/filters") { request, response ->
            val createPayload = mapper.readValue(request.body(), CreateFilterPayload::class.java)
            val created = logic.createFilter(createPayload)
            response.type(AION_VND)
            response.status(HttpStatus.SC_CREATED)
            mapper.writeValueAsString(created)
        }
    }

}