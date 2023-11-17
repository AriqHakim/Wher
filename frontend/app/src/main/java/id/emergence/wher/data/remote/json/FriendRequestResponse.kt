package id.emergence.wher.data.remote.json

import id.emergence.wher.domain.model.FriendRequest
import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestResponse(
    val id: String,
    val status: String,
    val requester: UserResponse,
)

fun FriendRequestResponse.asModel() = FriendRequest(id, status, requester.asModel())
