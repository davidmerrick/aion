package com.merricklabs.aion.params

import biweekly.component.VEvent
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import com.grum.geocalc.Coordinate
import com.grum.geocalc.EarthCalc
import com.grum.geocalc.Point
import com.merricklabs.aion.external.GeocoderClient

/**
 * Filters out any points outside of a radius in km
 */
@DynamoDBDocument
data class LocationFilter @JvmOverloads constructor(
        @DynamoDBAttribute var latitude: Double? = null,
        @DynamoDBAttribute var longitude: Double? = null,
        @DynamoDBAttribute var distanceKm: Int? = null
) {
    fun apply(event: VEvent, geocoderClient: GeocoderClient): Boolean {
        geocoderClient.fetchLocation(event.location.value)?.let {
            val sourcePoint = toPoint()
            return EarthCalc.harvesineDistance(sourcePoint, it) / 1000 < distanceKm!!.toDouble()
        }

        // If client couldn't geocode the location, fallback to allowing the event through
        return true
    }
}

fun LocationFilter.toPoint(): Point {
    val lat = Coordinate.fromDegrees(latitude!!)
    val lng = Coordinate.fromDegrees(longitude!!)
    return Point.at(lat, lng)
}