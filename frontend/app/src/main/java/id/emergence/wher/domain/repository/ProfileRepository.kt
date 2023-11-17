package id.emergence.wher.domain.repository

import id.emergence.wher.domain.model.ProfileData
import id.emergence.wher.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun fetchProfile(): Flow<Result<User>>

    fun updateProfile(data: ProfileData): Flow<Result<String>>

    fun deleteAccount(): Flow<Result<String>>
}
