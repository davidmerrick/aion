package com.merricklabs.aion.storage

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.models.AionCalendar
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.UUID

private val log = KotlinLogging.logger {}

class AionStorage : KoinComponent {

    private val mapper: DynamoDBMapper

    init {
        val config by inject<AionConfig>()
        val dynamoDbConfig = config.dynamoDb
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region))
                .build()
        val mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride(dynamoDbConfig.tableName))
                .build()
        mapper = DynamoDBMapper(client, mapperConfig)
    }

    fun saveCalendarFilter(calendar: AionCalendar) {
        log.debug("Saving calendar ${calendar.id} to db")
        mapper.save(calendar)
        log.debug("Saved ${calendar.id} to db")
    }

    fun getCalendar(id: UUID): AionCalendar? {
        log.debug("Retrieving calendar with id $id from db")
        val partitionKey = AionCalendar(id, "")
        val queryExpression = DynamoDBQueryExpression<AionCalendar>()
                .withHashKeyValues(partitionKey)
        val resultList = mapper.query(AionCalendar::class.java, queryExpression)
        return if (resultList.isEmpty()) {
            log.warn("Calendar with id $id not found in db.")
            null
        } else resultList[0]
    }
}