package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.HttpResponseException
import org.koin.core.KoinComponent
import org.koin.core.inject
import spark.Spark

open class AionResource : KoinComponent {

    protected val mapper by inject<ObjectMapper>()

    open fun defineResources() {
        Spark.exception(HttpResponseException::class.java) { e, _, response ->
            response.status(e.statusCode)
            response.body(e.message)
        }
    }
}