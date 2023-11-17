package id.emergence.wher.screen.friends.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FriendDetailViewModel(
    private val repo: FriendRepository,
    handle: SavedStateHandle,
) : BaseViewModel() {
    private val id = handle.get<String>("id") ?: ""

    private val mutableUser = MutableStateFlow<User?>(null)
    val user = mutableUser.asStateFlow()

    init {
        fetchDetail()
    }

    fun fetchDetail() {
        OneTimeEvent.Loading.send()
        viewModelScope.launch {
            repo
                .fetchProfile(id)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        mutableUser.emit(result.getOrNull())
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }

    fun onRequestFriend(cancel: Boolean) {
        OneTimeEvent.Loading.send()
        viewModelScope.launch {
            repo
                .requestFriend(id, cancel)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        OneTimeEvent.Alert(result.getOrNull()).send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }

    fun onRemoveFriend() {
        OneTimeEvent.Loading.send()
        viewModelScope.launch {
            repo
                .removeFriend(id)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        OneTimeEvent.Alert(result.getOrNull()).send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }

    fun acceptFriendRequest() {
        OneTimeEvent.Loading.send()
        viewModelScope.launch {
            repo
                .acceptFriendRequest(id)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        OneTimeEvent.Alert(result.getOrNull()).send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }

    fun rejectFriendRequest() {
        OneTimeEvent.Loading.send()
        viewModelScope.launch {
            repo
                .rejectFriendRequest(id)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        OneTimeEvent.Alert(result.getOrNull()).send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }
}
