package com.merricklabs.aion.resources

import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.BeanConfig
import org.reflections.Reflections
import spark.Spark

class SwaggerResource : AionResource() {

    override fun defineResources() {
        super.defineResources()

        Spark.get("/swagger") { _, _ ->
            val beanConfig = BeanConfig()
            val packageName = "com.merricklabs.aion.resources"
            beanConfig.resourcePackage = packageName
            beanConfig.scan = true
            beanConfig.scanAndRead()

            val reflections = Reflections(packageName)
            val classes = reflections.t
            val swagger = Reader(beanConfig.swagger)
                    .read()
            mapper.writeValueAsString(swagger)
        }
    }
}