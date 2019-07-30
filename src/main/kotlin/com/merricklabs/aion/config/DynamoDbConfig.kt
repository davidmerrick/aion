package com.merricklabs.aion.config

class DynamoDbConfig {
    val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "http://localhost:4570"
    val region = System.getenv("DYNAMODB_REGION") ?: "us-west-2"
    val calendarTableName= System.getenv("CALENDAR_TABLE_NAME") ?: "aion-calendar"
    val filterTableName= System.getenv("FILTER_TABLE_NAME") ?: "aion-filter"
}