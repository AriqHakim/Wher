package id.emergence.wher.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import id.emergence.wher.data.prefs.DataStoreManager
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.SafeApiRequest
import id.emergence.wher.data.remote.json.FriendRequestBody
import id.emergence.wher.data.remote.json.asModel
import id.emergence.wher.data.remote.paging.WherPagingConfig.NETWORK_PAGE_SIZE
import id.emergence.wher.data.remote.paging.WherPagingSource
import id.emergence.wher.data.remote.wrapFlowApiCall
import id.emergence.wher.domain.model.FriendRequest
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class FriendRepositoryImpl(
    private val api: ApiService,
    private val prefs: DataStoreManager,
) : SafeApiRequest(),
    FriendRepository {
    override fun fetchFriends(): Flow<PagingData<User>> =
        prefs.token.flatMapLatest { token ->
            Pager(
                PagingConfig(pageSize = NETWORK_PAGE_SIZE, initialLoadSize = NETWORK_PAGE_SIZE * 3, enablePlaceholders = true),
            ) {
                WherPagingSource(
                    source = { offset, limit ->
                        api.fetchFriends(token, offset, limit)
                    },
                )
            }.flow.map { pagingData -> pagingData.map { it.asModel() } }
        }

    override fun fetchFriendRequests(): Flow<PagingData<FriendRequest>> =
        prefs.token.flatMapLatest { token ->
            Pager(
                PagingConfig(pageSize = NETWORK_PAGE_SIZE, initialLoadSize = NETWORK_PAGE_SIZE * 3, enablePlaceholders = true),
            ) {
                WherPagingSource(
                    source = { offset, limit ->
                        api.fetchFriendRequest(token, offset, limit)
                    },
                )
            }.flow.map { pagingData -> pagingData.map { it.asModel() } }
        }

    override fun fetchProfile(id: String): Flow<Result<User>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.fetchProfile(token, id) }
                Result.success(response.asModel())
            }
        }

    override fun searchUsers(q: String): Flow<PagingData<User>> =
        prefs.token.flatMapLatest { token ->
            Pager(
                PagingConfig(pageSize = NETWORK_PAGE_SIZE, initialLoadSize = NETWORK_PAGE_SIZE * 3, enablePlaceholders = true),
            ) {
                WherPagingSource(
                    source = { offset, limit ->
                        api.searchUsers(token, q, offset, limit)
                    },
                )
            }.flow.map { pagingData -> pagingData.map { it.asModel() } }
        }

    override fun fetchSessionUserId(): Flow<String> = prefs.userId

    override suspend fun requestFriend(
        id: String,
        cancel: Boolean,
    ): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.requestFriend(token, id, body = FriendRequestBody(cancel)) }
                Result.success(response.message)
            }
        }

    override suspend fun acceptFriendRequest(id: String): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.acceptFriendRequest(token, id) }
                Result.success(response.message)
            }
        }

    override suspend fun rejectFriendRequest(id: String): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.rejectFriendRequest(token, id) }
                Result.success(response.message)
            }
        }

    override suspend fun removeFriend(id: String): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.removeFriend(token, id) }
                Result.success(response.message)
            }
        }
}
