package id.emergence.wher.domain.model

import id.emergence.wher.ext.hashEmail
import kotlin.random.Random

data class User(
    val id: String,
    val userId: String,
    val email: String,
    val username: String,
    val name: String,
    val photoUrl: String,
    val isFriend: Boolean = false,
    val requester: Boolean = false,
    val requestStatus: FriendRequestStatus = FriendRequestStatus.NONE,
) {
    val friendState: FriendState
        get() {
            return if (isFriend) {
                FriendState.FRIEND
            } else {
                if (requestStatus == FriendRequestStatus.PENDING && !requester) {
                    FriendState.INCOMING
                } else {
                    FriendState.NOT_FRIEND
                }
            }
        }

    val imgUrl
        get() =
            if (this.photoUrl.isNotEmpty()) {
                this.photoUrl
            } else if (this.email.isNotEmpty()) {
                "https://gravatar.com/avatar/${hashEmail(this.email)}"
            } else {
                val rng = Random.nextInt(144, 1000)
                "https://placekitten.com/$rng/$rng"
            }
}
