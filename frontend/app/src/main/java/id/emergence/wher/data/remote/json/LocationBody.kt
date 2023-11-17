package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class LocationBody(
    val lat: Double,
    val lon: Double,
)
