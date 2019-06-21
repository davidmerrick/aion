package com.merricklabs.aion

import com.merricklabs.aion.handlers.CalendarFilterHandler
import com.merricklabs.aion.handlers.CalendarFilterHandlerLogic
import com.merricklabs.aion.handlers.CalendarHandler
import com.merricklabs.aion.handlers.CalendarHandlerLogic
import com.merricklabs.aion.storage.AionStorage
import org.koin.dsl.module

val AionModule = module {
    single { CalendarFilterHandler() }
    single { CalendarFilterHandlerLogic() }
    single { CalendarHandler() }
    single { CalendarHandlerLogic() }
    single { AionStorage() }
}