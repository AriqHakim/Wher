package id.emergence.wher.domain.model

data class RegisterData(
    val email: String,
    val username: String,
    val name: String,
    val password: String,
    val confirmPassword: String,
)
