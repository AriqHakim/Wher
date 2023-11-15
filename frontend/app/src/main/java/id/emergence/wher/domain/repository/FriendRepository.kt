package id.emergence.wher.domain.repository

import androidx.paging.PagingData
import id.emergence.wher.domain.model.FriendRequest
import id.emergence.wher.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun fetchFriends(): Flow<PagingData<User>>

    fun fetchFriendRequests(): Flow<PagingData<FriendRequest>>

    fun fetchProfile(id: String): Flow<Result<User>>

    fun searchUsers(q: String): Flow<PagingData<User>>

    suspend fun requestFriend(
        id: String,
        cancel: Boolean,
    ): Flow<Result<String>>

    suspend fun acceptFriendRequest(id: String): Flow<Result<String>>

    suspend fun rejectFriendRequest(id: String): Flow<Result<String>>

    suspend fun removeFriend(id: String): Flow<Result<String>>
}
