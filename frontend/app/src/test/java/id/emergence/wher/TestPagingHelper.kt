package id.emergence.wher

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListUpdateCallback
import kotlin.coroutines.CoroutineContext

class TestPagingSource<T : Any> : PagingSource<Int, T>() {
    companion object {
        fun <T : Any> snapshot(items: List<T>): PagingData<T> = PagingData.from(items)
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = LoadResult.Page(emptyList(), 0, 1)
}

fun <T : Any> buildDiffer(
    diffCallback: ItemCallback<T>,
    dispatcher: CoroutineContext,
) = AsyncPagingDataDiffer(
    diffCallback = diffCallback,
    updateCallback =
        object : ListUpdateCallback {
            override fun onInserted(
                position: Int,
                count: Int,
            ) {
            }

            override fun onRemoved(
                position: Int,
                count: Int,
            ) {
            }

            override fun onMoved(
                fromPosition: Int,
                toPosition: Int,
            ) {
            }

            override fun onChanged(
                position: Int,
                count: Int,
                payload: Any?,
            ) {
            }
        },
    mainDispatcher = dispatcher,
    workerDispatcher = dispatcher,
)
