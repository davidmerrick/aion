package com.merricklabs.aion.external

import com.merricklabs.aion.config.AionConfig
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

private const val GOOGLE_GEOCODER_URL = "https://maps.googleapis.com/maps/api/geocode/json"

class GeocoderClient : KoinComponent {

    private val okHttpClient by inject<OkHttpClient>()
    private val apiKey: String

    init {
        val config by inject<AionConfig>()
        apiKey = config.geocoder.apiKey
    }

    fun fetchLocation(address: String): LocationResult? {
        log.info("Fetching address from geocoder: $address")
        val request = Request.Builder()
                .url(GOOGLE_GEOCODER_URL)
                .get()
                .build()
        val response = okHttpClient.newCall(request).execute()

        response.body()?.let {
            return null
        }

        log.warn("Unable to geolocate address: $address")
        return null
    }
}