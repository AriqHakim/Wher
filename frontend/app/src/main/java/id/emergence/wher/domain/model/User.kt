package id.emergence.wher.domain.model

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
}
