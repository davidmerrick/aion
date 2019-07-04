package com.merricklabs.aion.storage

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.exceptions.CalendarNotFoundException
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.resources.models.AionCalendar
import com.merricklabs.aion.resources.models.toDomain
import com.merricklabs.aion.storage.models.DbAionCalendar
import com.merricklabs.aion.storage.models.toDb
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class CalendarStorage : KoinComponent {

    private val mapper: DynamoDBMapper

    init {
        val config by inject<AionConfig>()
        val mapperFactory by inject<DynamoMapperFactory>()
        mapper = mapperFactory.buildMapper(config.dynamoDb.calendarTableName)
    }

    fun saveCalendar(calendar: AionCalendar) {
        log.debug("Saving calendar ${calendar.id} to db")
        mapper.save(calendar.toDb())
        log.debug("Saved ${calendar.id} to db")
    }

    fun getCalendar(id: EntityId): AionCalendar {
        log.debug("Retrieving calendar with id $id from db")
        val partitionKey = DbAionCalendar(id = id)
        val queryExpression = DynamoDBQueryExpression<DbAionCalendar>()
                .withHashKeyValues(partitionKey)
        val resultList = mapper.query(DbAionCalendar::class.java, queryExpression)
        return if (resultList.isEmpty()) {
            log.warn("Calendar with id $id not found in db.")
            throw CalendarNotFoundException(id.toString())
        } else resultList[0].toDomain()
    }
}