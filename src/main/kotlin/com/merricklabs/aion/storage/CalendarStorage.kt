package com.merricklabs.aion.storage

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.models.AionCalendar
import com.merricklabs.aion.models.toDomain
import com.merricklabs.aion.storage.models.DbAionCalendar
import com.merricklabs.aion.storage.models.toDb
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.UUID

private val log = KotlinLogging.logger {}

class CalendarStorage : KoinComponent {

    private val mapper: DynamoDBMapper

    init {
        val config by inject<AionConfig>()
        val dynamoDbConfig = config.dynamoDb
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region))
                .build()
        val mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride(dynamoDbConfig.calendarTableName))
                .build()
        mapper = DynamoDBMapper(client, mapperConfig)
    }

    fun saveCalendar(calendar: AionCalendar) {
        log.debug("Saving calendar ${calendar.id} to db")
        mapper.save(calendar.toDb())
        log.debug("Saved ${calendar.id} to db")
    }

    fun getCalendar(id: UUID): AionCalendar? {
        log.debug("Retrieving calendar with id $id from db")
        val partitionKey = DbAionCalendar(id, "")
        val queryExpression = DynamoDBQueryExpression<DbAionCalendar>()
                .withHashKeyValues(partitionKey)
        val resultList = mapper.query(DbAionCalendar::class.java, queryExpression)
        return if (resultList.isEmpty()) {
            log.warn("Calendar with id $id not found in db.")
            null
        } else resultList[0].toDomain()
    }
}