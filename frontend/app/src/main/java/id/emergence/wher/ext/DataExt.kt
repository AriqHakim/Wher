package id.emergence.wher.ext

import java.security.MessageDigest

fun hashEmail(email: String): String {
    val bytes = email.trim().lowercase().toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}
