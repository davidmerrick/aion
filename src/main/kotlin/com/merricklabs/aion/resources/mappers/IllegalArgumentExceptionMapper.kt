package com.merricklabs.aion.resources.mappers

import org.apache.http.HttpStatus
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class IllegalArgumentExceptionMapper : ExceptionMapper<IllegalArgumentException> {
    override fun toResponse(exception: IllegalArgumentException): Response {
        return Response.status(HttpStatus.SC_BAD_REQUEST).entity(exception.message)
                .type("text/plain").build()
    }
}