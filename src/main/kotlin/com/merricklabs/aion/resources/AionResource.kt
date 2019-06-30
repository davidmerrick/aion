package com.merricklabs.aion.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.exceptions.InvalidAcceptHeadersException
import com.merricklabs.aion.exceptions.InvalidContentTypeException
import com.merricklabs.aion.handlers.util.AionHeaders.AION_VND
import org.apache.http.HttpHeaders.ACCEPT
import org.apache.http.HttpHeaders.CONTENT_TYPE
import org.apache.http.client.HttpResponseException
import org.koin.core.KoinComponent
import org.koin.core.inject
import spark.Request
import spark.Spark

open class AionResource : KoinComponent {

    protected val mapper by inject<ObjectMapper>()

    open fun defineResources() {
        Spark.exception(HttpResponseException::class.java) { e, _, response ->
            response.status(e.statusCode)
            response.body(e.message)
        }
    }

    protected fun validateAcceptHeaders(request: Request) {
        val header = request.headers(ACCEPT) ?: ""
        if (header.isEmpty() || !header.contains(AION_VND)) {
            throw InvalidAcceptHeadersException()
        }
    }

    protected fun validateContentTypeHeaders(request: Request) {
        val header = request.headers(CONTENT_TYPE) ?: ""
        if (header.isEmpty() || !header.contains(AION_VND)) {
            throw InvalidContentTypeException()
        }
    }
}