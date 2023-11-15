package id.emergence.wher.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import id.emergence.wher.data.prefs.DataStoreManager
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.SafeApiRequest
import id.emergence.wher.data.remote.json.LocationBody
import id.emergence.wher.data.remote.json.asModel
import id.emergence.wher.data.remote.paging.WherPagingConfig.NETWORK_PAGE_SIZE
import id.emergence.wher.data.remote.paging.WherPagingSource
import id.emergence.wher.data.remote.wrapFlowApiCall
import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.domain.model.Location
import id.emergence.wher.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(
    private val api: ApiService,
    private val prefs: DataStoreManager,
) : SafeApiRequest(),
    LocationRepository {
    override fun fetchLocations(): Flow<PagingData<FriendLocation>> =
        prefs.token.flatMapLatest { token ->
            Pager(
                PagingConfig(pageSize = NETWORK_PAGE_SIZE, initialLoadSize = NETWORK_PAGE_SIZE * 3, enablePlaceholders = true),
            ) {
                WherPagingSource(
                    source = { offset, limit ->
                        api.fetchLocations(token, offset, limit)
                    },
                )
            }.flow.map { pagingData -> pagingData.map { it.asModel() } }
        }

    override suspend fun updateLocation(data: Location): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.updateLocation(token, LocationBody(data.lat, data.lon)) }
                Result.success(response.message)
            }
        }
}
