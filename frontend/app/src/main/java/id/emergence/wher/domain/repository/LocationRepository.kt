package id.emergence.wher.domain.repository

import androidx.paging.PagingData
import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun fetchLocations(): Flow<PagingData<FriendLocation>>

    suspend fun updateLocation(data: Location): Flow<Result<String>>
}
