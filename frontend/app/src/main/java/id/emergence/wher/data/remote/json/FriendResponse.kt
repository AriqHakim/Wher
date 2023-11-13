package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class FriendResponse(
    val userId: String,
    val username: String,
    val name: String,
    val email: String,
    val isFriend: String,
    val requester: Boolean,
    val requestStatus: String,
)
