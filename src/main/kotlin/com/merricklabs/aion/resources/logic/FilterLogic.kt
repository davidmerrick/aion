package com.merricklabs.aion.resources.logic

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.params.EntityId
import com.merricklabs.aion.resources.models.AionFilter
import com.merricklabs.aion.resources.models.CreateFilterPayload
import com.merricklabs.aion.resources.models.toDomain
import com.merricklabs.aion.storage.FilterStorage
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class FilterLogic : KoinComponent {

    private val storage by inject<FilterStorage>()
    private val mapper by inject<ObjectMapper>()

    fun getFilter(id: EntityId) = storage.getFilter(id)

    fun createFilter(createPayload: CreateFilterPayload): AionFilter {
        val toCreate = createPayload.toDomain()
        storage.saveFilter(toCreate)
        return storage.getFilter(toCreate.id)
    }
}