package id.emergence.wher.domain.model

data class User(
    val id: String,
    val userId: String,
    val username: String,
    val name: String,
    val photoUrl: String,
    val isFriend: Boolean = true,
    val requester: Boolean = true,
    val requestStatus: FriendRequestStatus,
)
