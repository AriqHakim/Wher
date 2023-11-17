package id.emergence.wher.data.remote.json

import kotlinx.serialization.Serializable

@Serializable
data class RegisterBody(
    val email: String,
    val username: String,
    val name: String,
    val password: String,
    val confirmPassword: String,
)
