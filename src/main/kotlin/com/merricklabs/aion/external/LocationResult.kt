package com.merricklabs.aion.external

import com.grum.geocalc.Coordinate
import com.grum.geocalc.Point

data class LocationResult(val latitude: Double, val longitude: Double)

fun LocationResult.toPoint(): Point {
    val lat = Coordinate.fromDegrees(latitude)
    val lng = Coordinate.fromDegrees(longitude)
    return Point.at(lat, lng)
}