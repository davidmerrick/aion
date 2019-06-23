package com.merricklabs.aion

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.handlers.ApplyFilterHandler
import com.merricklabs.aion.handlers.CalendarHandler
import com.merricklabs.aion.handlers.FilterHandler
import com.merricklabs.aion.handlers.logic.ApplyFilterLogic
import com.merricklabs.aion.handlers.logic.CalendarLogic
import com.merricklabs.aion.handlers.logic.FilterLogic
import com.merricklabs.aion.storage.CalendarStorage
import com.merricklabs.aion.storage.DynamoMapperFactory
import com.merricklabs.aion.storage.FilterStorage
import com.merricklabs.aion.util.AionObjectMapper
import org.koin.dsl.module

val AionModule = module {
    single { AionObjectMapper() as ObjectMapper }
    single { AionConfig() }
    single { CalendarClient() }

    // Handlers
    single { FilterHandler() }
    single { FilterLogic() }
    single { ApplyFilterHandler() }
    single { ApplyFilterLogic() }
    single { CalendarHandler() }
    single { CalendarLogic() }

    // Storage
    single { FilterStorage() }
    single { CalendarStorage() }
    single { DynamoMapperFactory() }
}