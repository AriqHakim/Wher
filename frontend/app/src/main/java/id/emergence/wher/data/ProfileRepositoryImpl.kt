package id.emergence.wher.data

import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.domain.model.ProfileData
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryImpl(
    private val api: ApiService,
) : ProfileRepository {
    override fun fetchProfile(): Flow<Result<User>> {
        TODO("Not yet implemented")
    }

    override fun updateProfile(data: ProfileData): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(): Flow<Result<String>> {
        TODO("Not yet implemented")
    }
}
