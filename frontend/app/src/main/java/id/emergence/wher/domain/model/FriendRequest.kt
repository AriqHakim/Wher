package id.emergence.wher.domain.model

data class FriendRequest(
    val id: String,
    val status: String,
    val requester: User,
)
