package id.emergence.wher.data

import androidx.paging.PagingData
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.SafeApiRequest
import id.emergence.wher.domain.model.FriendRequest
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow

class FriendRepositoryImpl(
    private val api: ApiService,
) : SafeApiRequest(),
    FriendRepository {
    override fun fetchFriends(): Flow<PagingData<User>> {
        TODO("Not yet implemented")
    }

    override fun fetchFriendRequests(): Flow<PagingData<FriendRequest>> {
        TODO("Not yet implemented")
    }

    override fun fetchProfile(id: String): Flow<Result<User>> {
        TODO("Not yet implemented")
    }

    override fun searchUsers(q: String): Flow<PagingData<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun requestFriend(id: String): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun acceptFriendRequest(id: String): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun rejectFriendRequest(id: String): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFriend(id: String): Flow<Result<String>> {
        TODO("Not yet implemented")
    }
}
