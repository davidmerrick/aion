package com.merricklabs.aion

import com.merricklabs.aion.config.AionConfig
import com.merricklabs.aion.external.CalendarClient
import com.merricklabs.aion.handlers.CalendarExportHandler
import com.merricklabs.aion.handlers.logic.CalendarExportHandlerLogic
import com.merricklabs.aion.handlers.CalendarFilterHandler
import com.merricklabs.aion.handlers.logic.CalendarFilterHandlerLogic
import com.merricklabs.aion.storage.AionStorage
import org.koin.dsl.module

val AionModule = module {
    single { CalendarFilterHandler() }
    single { CalendarFilterHandlerLogic() }
    single { CalendarExportHandler() }
    single { CalendarExportHandlerLogic() }
    single { AionStorage() }
    single { AionConfig() }
    single { CalendarClient() }
}