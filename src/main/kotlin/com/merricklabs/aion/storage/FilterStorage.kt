package com.merricklabs.aion.storage

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.models.AionFilter
import com.merricklabs.aion.models.toDomain
import com.merricklabs.aion.storage.models.DbAionFilter
import com.merricklabs.aion.storage.models.toDb
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.UUID

private val log = KotlinLogging.logger {}

class FilterStorage : KoinComponent {

    private val mapper: DynamoDBMapper

    init {
        val config by inject<AionConfig>()
        val mapperFactory by inject<DynamoMapperFactory>()
        mapper = mapperFactory.buildMapper(config.dynamoDb.filterTableName)
    }

    fun saveFilter(filter: AionFilter) {
        log.debug("Saving calendar ${filter.id} to db")
        mapper.save(filter.toDb())
        log.debug("Saved ${filter.id} to db")
    }

    fun getFilter(id: UUID): AionFilter? {
        log.debug("Retrieving calendar with id $id from db")
        val partitionKey = DbAionFilter(id = id)
        val queryExpression = DynamoDBQueryExpression<DbAionFilter>()
                .withHashKeyValues(partitionKey)
        val resultList = mapper.query(DbAionFilter::class.java, queryExpression)
        return if (resultList.isEmpty()) {
            log.warn("Calendar with id $id not found in db.")
            null
        } else resultList[0].toDomain()
    }
}