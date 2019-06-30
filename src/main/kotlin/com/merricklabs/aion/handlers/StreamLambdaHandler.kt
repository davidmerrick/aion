package com.merricklabs.aion.handlers

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.merricklabs.aion.AionModule
import com.merricklabs.aion.resources.CalendarResource
import com.merricklabs.aion.resources.FilterResource
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.get
import spark.Spark
import java.io.InputStream
import java.io.OutputStream

class StreamLambdaHandler : RequestStreamHandler, KoinComponent {

    private val handler: SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>

    init {
        try {
            startKoin {
                modules(AionModule)
            }
            handler = SparkLambdaContainerHandler.getAwsProxyHandler()
            defineResources()
            Spark.awaitInitialization()
        } catch (e: ContainerInitializationException) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace()
            throw RuntimeException("Could not initialize Spark container", e)
        }
    }

    private fun defineResources() {
        val calendarResource: CalendarResource = get()
        calendarResource.defineResources()

        val filterResource: FilterResource = get()
        filterResource.defineResources()
    }

    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler.proxyStream(inputStream, outputStream, context)
    }
}