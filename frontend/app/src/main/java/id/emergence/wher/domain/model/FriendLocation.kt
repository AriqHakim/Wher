package id.emergence.wher.domain.model

data class FriendLocation(
    val id: String,
    val username: String,
    val photoUrl: String,
    val location: Location,
)
