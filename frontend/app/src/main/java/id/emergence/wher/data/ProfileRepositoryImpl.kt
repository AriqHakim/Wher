package id.emergence.wher.data

import id.emergence.wher.data.prefs.DataStoreManager
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.SafeApiRequest
import id.emergence.wher.data.remote.json.asModel
import id.emergence.wher.data.remote.wrapFlowApiCall
import id.emergence.wher.domain.model.ProfileData
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class ProfileRepositoryImpl(
    private val api: ApiService,
    private val prefs: DataStoreManager,
) : SafeApiRequest(),
    ProfileRepository {
    override fun fetchProfile(): Flow<Result<User>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.fetchProfile(token) }
                Result.success(response.asModel())
            }
        }

    override fun updateProfile(data: ProfileData): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override fun deleteAccount(): Flow<Result<String>> {
        TODO("Not yet implemented")
    }
}
