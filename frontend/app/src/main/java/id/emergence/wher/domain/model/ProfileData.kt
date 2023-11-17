package id.emergence.wher.domain.model

import java.io.File

data class ProfileData(
    val name: String,
    val password: String,
    val confirmPassword: String,
    val file: File,
)
