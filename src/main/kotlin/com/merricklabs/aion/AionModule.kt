package com.merricklabs.aion

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.resources.CalendarResource
import com.merricklabs.aion.resources.FilterResource
import com.merricklabs.aion.resources.ResourceConfigBuilder
import com.merricklabs.aion.resources.logic.CalendarLogic
import com.merricklabs.aion.resources.logic.FilterLogic
import com.merricklabs.aion.storage.CalendarStorage
import com.merricklabs.aion.storage.DynamoMapperFactory
import com.merricklabs.aion.storage.FilterStorage
import com.merricklabs.aion.util.AionObjectMapper
import okhttp3.OkHttpClient
import org.koin.dsl.module

val AionModule = module {
    single { AionObjectMapper() as ObjectMapper }
    single { AionConfig() }

    // Clients
    single { OkHttpClient() }
    single { CalendarClient() }
    single { GeocoderClient() }

    // Resources
    single { CalendarResource() }
    single { FilterResource() }
    single { ResourceConfigBuilder() }

    // Logic
    single { FilterLogic() }
    single { CalendarLogic() }

    // Storage
    single { FilterStorage() }
    single { CalendarStorage() }
    single { DynamoMapperFactory() }
}