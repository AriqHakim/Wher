package id.emergence.wher.screen.friends.list

import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.utils.base.BaseViewModel

class FriendListViewModel(
    repo: FriendRepository,
) : BaseViewModel() {
    val friends = repo.fetchFriends()
}
