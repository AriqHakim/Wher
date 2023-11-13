package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val userId: String,
    val username: String,
    val name: String,
    val photoUrl: String,
)
