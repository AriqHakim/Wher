package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class FriendLocationResponse(
    val id: String,
    val username: String,
    val photoUrl: String,
    val location: LocationResponse,
)
