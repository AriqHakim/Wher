package id.emergence.wher.data.remote.json

import id.emergence.wher.domain.model.FriendRequestStatus
import id.emergence.wher.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String = "",
    val userId: String = "",
    val email: String = "",
    val username: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val isFriend: Boolean = false,
    val requester: Boolean = false,
    val requestStatus: String = "",
)

fun UserResponse.asModel() =
    User(
        id = id,
        userId = userId,
        email = email,
        username = username,
        name = name,
        photoUrl = photoUrl,
        isFriend = isFriend,
        requester = requester,
        requestStatus = FriendRequestStatus.from(requestStatus),
    )
