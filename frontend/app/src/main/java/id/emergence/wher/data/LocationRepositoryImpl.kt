package id.emergence.wher.data

import id.emergence.wher.data.prefs.DataStoreManager
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.SafeApiRequest
import id.emergence.wher.data.remote.json.LocationBody
import id.emergence.wher.data.remote.json.asModel
import id.emergence.wher.data.remote.paging.WherPagingConfig.INITIAL_LOAD_SIZE
import id.emergence.wher.data.remote.paging.WherPagingConfig.NETWORK_PAGE_SIZE
import id.emergence.wher.data.remote.wrapFlowApiCall
import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.domain.model.Location
import id.emergence.wher.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocationRepositoryImpl(
    private val api: ApiService,
    private val prefs: DataStoreManager,
) : SafeApiRequest(),
    LocationRepository {
    override fun fetchLocations(page: Int): Flow<Result<List<FriendLocation>>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val offset = if (page != 1) (page * NETWORK_PAGE_SIZE) else INITIAL_LOAD_SIZE - 1
                val response = apiRequest { api.fetchLocations(token, offset, 30) }
                val list = response.data.ifEmpty { emptyList() }.map { it.asModel() }
                Result.success(list)
            }
        }

    override suspend fun updateLocation(data: Location): Flow<Result<String>> =
        prefs.token.flatMapLatest { token ->
            wrapFlowApiCall {
                val response = apiRequest { api.updateLocation(token, LocationBody(data.lat, data.lon)) }
                prefs.updateLastSharingTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                Result.success(response.message)
            }
        }

    override suspend fun toggleSharingLocation(flag: Boolean) {
        prefs.setSharingLocation(flag)
    }

    override fun checkIsSharingLocation(): Flow<Boolean> = prefs.isSharingLocation

    override fun getLastSharingTime(): Flow<String> = prefs.lastSharingTime
}
