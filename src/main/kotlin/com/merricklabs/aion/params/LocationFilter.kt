package com.merricklabs.aion.params

import biweekly.component.VEvent
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.grum.geocalc.DegreeCoordinate
import com.grum.geocalc.EarthCalc
import com.grum.geocalc.Point
import com.merricklabs.aion.external.GeocoderClient
import com.merricklabs.aion.external.toPoint


/**
 * Filters out any points outside of a radius in km
 */
data class LocationFilter @JvmOverloads constructor(
        @DynamoDBAttribute var latitude: Double?,
        @DynamoDBAttribute var longitude: Double?,
        @DynamoDBAttribute var radiusKm: Int?
) {
    fun apply(event: VEvent, geocoderClient: GeocoderClient): Boolean {
        val sourcePoint = toPoint()
        val targetPoint = geocoderClient.fetchLocation(event.location.value)
                .toPoint()
        return EarthCalc.getHarvesineDistance(sourcePoint, targetPoint) / 1000 < radiusKm!!.toDouble()
    }
}

fun LocationFilter.toPoint(): Point {
    val lat = DegreeCoordinate(latitude!!)
    val lng = DegreeCoordinate(longitude!!)
    return Point(lat, lng)
}