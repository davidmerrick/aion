package com.merricklabs.aion.handlers

import com.amazonaws.serverless.proxy.jersey.JerseyLambdaContainerHandler
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.resources.ResourceConfigBuilder
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

        val resourceConfigBuilder: ResourceConfigBuilder = get()

        val jerseyApplication = resourceConfigBuilder.build()
        handler = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication)
    }

    @Throws(IOException::class)
    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler.proxyStream(inputStream, outputStream, context)
    }
}