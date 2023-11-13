package id.emergence.wher.data.remote.json

data class FriendRequestResponse(
    val id: String,
    val status: String,
    val requester: UserResponse,
)
