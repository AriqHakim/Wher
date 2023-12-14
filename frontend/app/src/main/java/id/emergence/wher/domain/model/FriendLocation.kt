package id.emergence.wher.domain.model

import id.emergence.wher.ext.hashEmail
import kotlin.random.Random

data class FriendLocation(
    val id: String,
    val username: String,
    val email: String,
    val photoUrl: String,
    val location: Location,
) {
    val imgUrl =
        if (this.photoUrl.isNotEmpty()) {
            this.photoUrl
        } else if (this.email.isNotEmpty()) {
            "https://gravatar.com/avatar/${hashEmail(this.email)}"
        } else {
            val rng = Random.nextInt(144, 1000)
            "https://placekitten.com/$rng/$rng"
        }
}
