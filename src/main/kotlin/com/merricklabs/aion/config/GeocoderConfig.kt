package com.merricklabs.aion.config

class GeocoderConfig {
    // Todo: Mock this out better for testing
    val apiKey = System.getenv("GEOCODER_API_KEY") ?: "fakeApiKey"
}