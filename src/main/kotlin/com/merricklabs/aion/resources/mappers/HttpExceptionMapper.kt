package com.merricklabs.aion.resources.mappers

import org.apache.http.client.HttpResponseException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class HttpExceptionMapper : ExceptionMapper<HttpResponseException> {
    override fun toResponse(exception: HttpResponseException): Response {
        return Response.status(exception.statusCode).entity(exception.message)
                .type("text/plain").build()
    }
}