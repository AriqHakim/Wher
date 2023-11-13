package id.emergence.wher.data

import androidx.paging.PagingData
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.domain.model.Location
import id.emergence.wher.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val api: ApiService,
) : LocationRepository {
    override fun fetchLocations(): Flow<PagingData<FriendLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateLocation(data: Location): Flow<Result<String>> {
        TODO("Not yet implemented")
    }
}
