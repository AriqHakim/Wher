package id.emergence.wher.domain.model

enum class FriendRequestStatus(
    val key: String,
) {
    NONE("none"),
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    ;

    companion object {
        private val MAP: Map<String, FriendRequestStatus> = FriendRequestStatus.values().associateBy { it.key }

        infix fun from(key: String) = MAP[key] ?: NONE
    }
}
