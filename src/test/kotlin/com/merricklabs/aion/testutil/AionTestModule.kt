package com.merricklabs.aion.testutil
import org.koin.dsl.module

val AionTestModule = module {
    single { DynamoTestClient() }
}