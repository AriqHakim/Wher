package id.emergence.wher.data

import id.emergence.wher.data.prefs.DataStoreManager
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.SafeApiRequest
import id.emergence.wher.data.remote.json.LoginBody
import id.emergence.wher.data.remote.json.RegisterBody
import id.emergence.wher.data.remote.wrapFlowApiCall
import id.emergence.wher.domain.model.LoginData
import id.emergence.wher.domain.model.RegisterData
import id.emergence.wher.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val api: ApiService,
    private val prefs: DataStoreManager,
) : SafeApiRequest(),
    AuthRepository {
    override suspend fun login(data: LoginData): Flow<Result<String>> =
        wrapFlowApiCall {
            val body = LoginBody(data.username, data.password)
            val response =
                apiRequest {
                    api.login(body)
                }
            val profileResponse =
                apiRequest {
                    api.fetchProfile("Bearer ${response.token}")
                }
            prefs.addSession(response.token, profileResponse.userId)
            Result.success("Login berhasil!")
        }

    override suspend fun register(data: RegisterData): Flow<Result<String>> =
        wrapFlowApiCall {
            val body = RegisterBody(data.email, data.username, data.name, data.password, data.confirmPassword)
            val response =
                apiRequest {
                    api.register(body)
                }
            Result.success(response.message)
        }

    override suspend fun logout() {
        prefs.clearSession()
    }
}
