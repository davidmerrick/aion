package com.merricklabs.aion.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.servlet.HandlerAdapter
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Configuration
@EnableWebMvc
@Profile("lambda")
open class Config {

    /**
     * Create required HandlerMapping, to avoid several default HandlerMapping instances being created
     */
    @Bean
    open fun handlerMapping(): HandlerMapping {
        return RequestMappingHandlerMapping()
    }

    /**
     * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created
     */
    @Bean
    open fun handlerAdapter(): HandlerAdapter {
        return RequestMappingHandlerAdapter()
    }

    /**
     * optimization - avoids creating default exception resolvers; not required as the serverless container handles
     * all exceptions
     *
     * By default, an ExceptionHandlerExceptionResolver is created which creates many dependent object, including
     * an expensive ObjectMapper instance.
     */
    @Bean
    open fun handlerExceptionResolver(): HandlerExceptionResolver {
        return HandlerExceptionResolver { _, _, _, _ -> null }
    }

}