package com.merricklabs.aion.testutil

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.config.DynamoDbConfig
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class DynamoTestClient : KoinComponent {

    private val dynamoConfig: DynamoDbConfig
    private val dynamo: DynamoDB

    init {
        val config by inject<AionConfig>()
        dynamoConfig = config.dynamoDb
        val client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(dynamoConfig.endpoint, dynamoConfig.region))
                .build()

        dynamo = DynamoDB(client)
    }

    /**
     * Healthcheck for localstack container
     */
    fun isDynamoReady(): Boolean {
        return try {
            dynamo.listTables()
            true
        } catch (e: Exception) {
            log.warn("Dynamo healthcheck failed")
            false
        }
    }

    fun createTables() {
        deleteTables()
        val calendarTable = dynamo.createTable(
                dynamoConfig.calendarTableName,
                listOf(KeySchemaElement("id", KeyType.HASH)),
                listOf(AttributeDefinition("id", ScalarAttributeType.S)),
                ProvisionedThroughput(1L, 1L)
        )
        val filterTable = dynamo.createTable(
                dynamoConfig.filterTableName,
                listOf(KeySchemaElement("id", KeyType.HASH)),
                listOf(AttributeDefinition("id", ScalarAttributeType.S)),
                ProvisionedThroughput(1L, 1L)
        )

        calendarTable.waitForActive()
        filterTable.waitForActive()
    }

    private fun deleteTables() {
        deleteTable(dynamoConfig.calendarTableName)
        deleteTable(dynamoConfig.filterTableName)
    }

    private fun deleteTable(tableName: String) {
        val table = dynamo.getTable(tableName)
        try {
            table.delete()
            table.waitForDelete()
        } catch (e: ResourceNotFoundException) {
            // Table doesn't exist, which is fine
        }
    }
}