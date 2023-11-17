package id.emergence.wher.data.remote.json

import id.emergence.wher.domain.model.Location
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val lat: Double,
    val lon: Double,
)

fun LocationResponse.asModel() = Location(lat, lon)
