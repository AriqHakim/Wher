package id.emergence.wher.screen.friends.search

import androidx.lifecycle.SavedStateHandle
import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.utils.base.BaseViewModel

class FriendSearchViewModel(
    private val repo: FriendRepository,
    handle: SavedStateHandle,
) : BaseViewModel() {
    private val query = handle.get<String>("query") ?: ""

    val user = repo.searchUsers(query)
}
