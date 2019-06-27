package com.merricklabs.aion.params

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute

data class LocationFilter @JvmOverloads constructor(
        @DynamoDBAttribute var latitude: Long?,
        @DynamoDBAttribute var longitude: Long?,
        @DynamoDBAttribute var radius: Int?
)