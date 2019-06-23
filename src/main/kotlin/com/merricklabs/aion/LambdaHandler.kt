package com.merricklabs.aion

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class LambdaHandler : RequestStreamHandler {

    @Throws(IOException::class)
    override fun handleRequest(inputStream: InputStream, outputStream: OutputStream, context: Context) {
        handler!!.proxyStream(inputStream, outputStream, context)

        // just in case it wasn't closed by the mapper
        outputStream.close()
    }

    companion object {

        private var handler: SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>? = null

        init {
            try {
                handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Application::class.java)
                handler!!.activateSpringProfiles("lambda")
            } catch (e: ContainerInitializationException) {
                // Re-throw the exception to force another cold start
                e.printStackTrace()
                throw RuntimeException("Could not initialize Spring Boot application", e)
            }

        }
    }
}