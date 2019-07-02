package com.merricklabs.aion.resources

import io.swagger.annotations.Info
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
            val classes = reflections.getTypesAnnotatedWith(Info::class.java)
            val swagger = Reader(beanConfig.swagger)
                    .read(classes)
            mapper.writeValueAsString(swagger)
        }
    }
}