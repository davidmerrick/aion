package com.merricklabs.aion.config

class DynamoDbConfig {
    val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "http://localhost:8000"
    val region = System.getenv("DYNAMODB_REGION") ?: "us-east-1"
    val calendarTableName= System.getenv("CALENDAR_TABLE_NAME") ?: "aion-calendar"
    val filterTableName= System.getenv("FILTER_TABLE_NAME") ?: "aion-filter"
}