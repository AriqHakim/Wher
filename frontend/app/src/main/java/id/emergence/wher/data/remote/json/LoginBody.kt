package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    val username: String,
    val password: String,
)
