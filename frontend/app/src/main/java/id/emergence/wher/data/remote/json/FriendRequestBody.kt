package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestBody(
    val cancel: Boolean = false,
)
