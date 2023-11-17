package id.emergence.wher.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationResponse<T>(
    val data: List<T> = emptyList(),
    @SerialName("total_data") val total: Int = -1,
)
