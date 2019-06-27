package com.merricklabs.aion.storage.util

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.merricklabs.aion.params.EntityId

class EntityIdTypeConverter : DynamoDBTypeConverter<String, EntityId> {
    override fun unconvert(input: String?) = EntityId(input!!)
    override fun convert(input: EntityId?) = input!!.value
}