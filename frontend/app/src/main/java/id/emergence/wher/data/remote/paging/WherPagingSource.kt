package id.emergence.wher.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.emergence.wher.data.remote.json.PaginationResponse
import id.emergence.wher.data.remote.paging.WherPagingConfig.INITIAL_LOAD_SIZE
import id.emergence.wher.data.remote.paging.WherPagingConfig.NETWORK_PAGE_SIZE
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class WherPagingSource<T : Any>(
    private val source: suspend (offset: Int, limit: Int) -> Response<PaginationResponse<T>>,
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: INITIAL_LOAD_SIZE
        val offset = if (params.key != null) ((position - 1) * NETWORK_PAGE_SIZE) else INITIAL_LOAD_SIZE - 1
        return try {
            val response = source(offset, NETWORK_PAGE_SIZE)
            val pagedResponse = response.body()
            val data: List<T>? = pagedResponse?.data

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = if (data.isNullOrEmpty()) null else position + (params.loadSize / NETWORK_PAGE_SIZE),
            )
        } catch (ex: HttpException) {
            throw IOException(ex)
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        }
    }
}
