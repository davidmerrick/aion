package com.merricklabs.aion.storage

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.merricklabs.aion.config.AionConfig
import org.koin.core.KoinComponent
import org.koin.core.inject

class DynamoMapperFactory : KoinComponent {

    private val client: AmazonDynamoDB

    init {
        val config by inject<AionConfig>()
        val dynamoDbConfig = config.dynamoDb
        client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(dynamoDbConfig.endpoint, dynamoDbConfig.region))
                .build()
    }

    fun buildMapper(tableName: String): DynamoDBMapper {
        val mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(DynamoDBMapperConfig.TableNameOverride(tableName))
                .build()
        return DynamoDBMapper(client, mapperConfig)
    }
}