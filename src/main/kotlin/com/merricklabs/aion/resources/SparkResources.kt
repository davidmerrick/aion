package com.merricklabs.aion.resources

import com.merricklabs.aion.handlers.HelloModule
import org.koin.core.KoinComponent
import org.koin.core.inject
import spark.Spark

class SparkResources : KoinComponent {
    fun defineResources() {
        val helloModule by inject<HelloModule>()

        Spark.get("/hello") { _, _ -> helloModule.sayHello() }
    }
}