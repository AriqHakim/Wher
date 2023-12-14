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
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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

    override fun updateProfile(data: ProfileData): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                if (data.file == null) throw IllegalStateException("File is null")
                val requestBodyMap =
                    hashMapOf(
                        "name" to data.name.toRequestBody(),
                        "password" to data.password.toRequestBody(),
                        "confirmPassword" to data.confirmPassword.toRequestBody(),
                    )
                val photo = MultipartBody.Part.createFormData("file", data.file.name, data.file.asRequestBody())
                val response =
                    apiRequest {
                        api.editProfile(token, requestBodyMap, photo)
                    }
                Result.success(response.message)
            }
        }

    override fun deleteAccount(): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.deleteAccount(token) }
                Result.success(response.message)
            }
        }
}
