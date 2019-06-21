package com.merricklabs.aion.storage

import com.merricklabs.aion.config.AionConfig
import org.koin.core.KoinComponent
import org.koin.core.inject

class AionStorage : KoinComponent {
    val config by inject<AionConfig>()
}