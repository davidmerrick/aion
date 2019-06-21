package com.merricklabs.aion.config

class DynamoDbConfig {
    val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "https://dynamodb.us-west-2.amazonaws.com"
    val region = System.getenv("DYNAMODB_REGION") ?: "us-west-2"
    val tableName= System.getenv("DYNAMODB_TABLE_NAME") ?: "aion"
}