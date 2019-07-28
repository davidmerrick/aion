package com.merricklabs.aion.resources.util

import com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS
import com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS
import com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
import java.io.IOException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter

class CorsFilter : ContainerResponseFilter {

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {

        val headers = responseContext.headers

        headers.add(ACCESS_CONTROL_ALLOW_ORIGIN, "*")
        headers.add(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, DELETE, PUT")
        headers.add(ACCESS_CONTROL_ALLOW_HEADERS, "X-Requested-With, Content-Type")
    }
}