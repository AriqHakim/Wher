package id.emergence.wher.domain.repository

import id.emergence.wher.domain.model.LoginData
import id.emergence.wher.domain.model.RegisterData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(data: LoginData): Flow<Result<String>>

    suspend fun register(data: RegisterData): Flow<Result<String>>

    suspend fun logout()
}
