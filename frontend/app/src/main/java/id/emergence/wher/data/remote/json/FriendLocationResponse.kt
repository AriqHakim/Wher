package id.emergence.wher.data.remote.json

import id.emergence.wher.domain.model.FriendLocation
import kotlinx.serialization.Serializable

@Serializable
data class FriendLocationResponse(
    val id: String,
    val username: String,
    val photoUrl: String,
    val location: LocationResponse,
)

fun FriendLocationResponse.asModel() = FriendLocation(id, username, photoUrl, location.asModel())
