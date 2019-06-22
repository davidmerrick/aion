package com.merricklabs.aion.storage

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.config.DynamoDbConfig
import com.merricklabs.aion.models.CalendarFilter
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class AionStorage : KoinComponent {

    private val dynamoDbConfig: DynamoDbConfig
    private val client: AmazonDynamoDB

    init {
        val config by inject<AionConfig>()
        dynamoDbConfig = config.dynamoDb
        client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region))
                .build()

    }

    fun saveCalendarFilter(url: String) {
        log.info("Saving $url to db")
        val filter = CalendarFilter.create(url)
        val mapper = DynamoDBMapper(client)
        mapper.save(filter)
        log.info("Saved $url to db")
    }
}