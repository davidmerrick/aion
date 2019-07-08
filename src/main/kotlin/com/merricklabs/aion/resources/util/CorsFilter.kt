package com.merricklabs.aion.resources.util

import java.io.IOException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter

class CorsFilter : ContainerResponseFilter {

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {

        val headers = responseContext.headers

        headers.add("Access-Control-Allow-Origin", "*")
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type")
    }
}