package com.merricklabs.aion.handlers

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.resources.AionExceptionMapper
import com.merricklabs.aion.resources.CalendarResource
import com.merricklabs.aion.resources.FilterResource
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.get
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class StreamLambdaHandler : RequestStreamHandler, KoinComponent {

    private val handler: JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>

    init {
        startKoin {
            modules(AionModule)
        }

        val calendarResource: CalendarResource = get()
        val filterResource: FilterResource = get()

        val jerseyApplication = ResourceConfig()
                .register(JacksonFeature::class.java)
                .register(calendarResource)
                .register(filterResource)
                .register(AionExceptionMapper::class.java)

        handler = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication)
    }

    @Throws(IOException::class)
    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler.proxyStream(inputStream, outputStream, context)
    }
}