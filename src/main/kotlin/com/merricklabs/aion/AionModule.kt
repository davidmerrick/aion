package com.merricklabs.aion

import com.merricklabs.aion.handlers.CalendarFilterHandler
import com.merricklabs.aion.handlers.CalendarFilterHandlerLogic
import com.merricklabs.aion.handlers.CalendarExportHandler
import com.merricklabs.aion.handlers.CalendarExportHandlerLogic
import com.merricklabs.aion.storage.AionStorage
import org.koin.dsl.module

val AionModule = module {
    single { CalendarFilterHandler() }
    single { CalendarFilterHandlerLogic() }
    single { CalendarExportHandler() }
    single { CalendarExportHandlerLogic() }
    single { AionStorage() }
}