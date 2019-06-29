package com.merricklabs.aion.resources

import spark.Spark

object SparkResources {
    fun defineResources() {
        Spark.get("/hello") { _, _ -> "Hello World" }
    }
}