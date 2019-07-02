package com.merricklabs.aion.external

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.grum.geocalc.Coordinate
import com.grum.geocalc.Point
import com.merricklabs.aion.config.AionConfig
import mu.KotlinLogging
import org.koin.core.KoinComponent
import org.koin.core.inject

private val log = KotlinLogging.logger {}

class GeocoderClient : KoinComponent {

    private val apiKey: String

    init {
        val config by inject<AionConfig>()
        apiKey = config.geocoder.apiKey
    }

    fun fetchLocation(address: String): Point? {
        log.info("Fetching address from geocoder: $address")

        val context = GeoApiContext.Builder()
                .apiKey(apiKey)
                .build()
        val results = GeocodingApi.geocode(context, address).await()
        if (results[0].partialMatch) {
            return null
        }

        return Point.at(
                Coordinate.fromDegrees(results[0].geometry.location.lat),
                Coordinate.fromDegrees(results[0].geometry.location.lng)
        )
    }
}