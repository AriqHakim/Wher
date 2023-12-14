package id.emergence.wher.screen.friends.request

import androidx.paging.filter
import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.utils.base.BaseViewModel
import kotlinx.coroutines.flow.map

class FriendRequestViewModel(
    repo: FriendRepository,
) : BaseViewModel() {
    val requests = repo.fetchFriendRequests().map { p -> p.filter { it.status == "Pending" } }
}
