package com.merricklabs.aion.resources

import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.handlers.models.CreateFilterPayload
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import com.merricklabs.aion.params.EntityId
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus
import org.koin.core.inject
import spark.Spark.get
import spark.Spark.post

class FilterResource : AionResource() {

    private val logic by inject<FilterLogic>()

    override fun defineResources() {
        super.defineResources()

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
    }

}