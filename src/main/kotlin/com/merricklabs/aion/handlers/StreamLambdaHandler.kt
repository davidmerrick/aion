package com.merricklabs.aion.handlers

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.merricklabs.aion.resources.SparkResources
import spark.Spark
import java.io.InputStream
import java.io.OutputStream

class StreamLambdaHandler : RequestStreamHandler {

    private val handler: SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>

    init {
        try {
            handler = SparkLambdaContainerHandler.getAwsProxyHandler()
            SparkResources.defineResources()
            Spark.awaitInitialization()
        } catch (e: ContainerInitializationException) {
            // if we fail here. We re-throw the exception to force another cold start
            e.printStackTrace()
            throw RuntimeException("Could not initialize Spark container", e)
        }
    }

    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler.proxyStream(inputStream, outputStream, context)
    }
}