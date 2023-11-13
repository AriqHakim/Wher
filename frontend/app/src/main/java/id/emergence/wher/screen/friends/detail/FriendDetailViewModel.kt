package id.emergence.wher.screen.friends.detail

import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.utils.base.BaseViewModel

class FriendDetailViewModel(
    private val friends: FriendRepository,
) : BaseViewModel()
