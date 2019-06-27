package com.merricklabs.aion.external

import com.grum.geocalc.DegreeCoordinate
import com.grum.geocalc.Point

data class LocationResult(val latitude: Double, val longitude: Double)

fun LocationResult.toPoint(): Point {
    val lat = DegreeCoordinate(latitude!!)
    val lng = DegreeCoordinate(longitude!!)
    return Point(lat, lng)
}