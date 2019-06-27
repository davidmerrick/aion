package com.merricklabs.aion.external

import mu.KotlinLogging
import okhttp3.OkHttpClient
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

private const val GOOGLE_GEOCODER_URL = "https://maps.googleapis.com/maps/api/geocode/json"

class GeocoderClient : KoinComponent {

    private val okHttpClient by inject<OkHttpClient>()

    fun fetchLocation(address: String): LocationResult {
        // Todo: Implement this
        return LocationResult(0.0, 0.0)
    }
}