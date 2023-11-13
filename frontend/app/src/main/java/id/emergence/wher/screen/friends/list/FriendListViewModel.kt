package id.emergence.wher.screen.friends.list

import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.utils.base.BaseViewModel

class FriendListViewModel(
    private val friends: FriendRepository,
) : BaseViewModel()
