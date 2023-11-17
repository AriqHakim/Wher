package id.emergence.wher.domain.repository

import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun fetchLocations(page: Int): Flow<Result<List<FriendLocation>>>

    suspend fun updateLocation(data: Location): Flow<Result<String>>
}
