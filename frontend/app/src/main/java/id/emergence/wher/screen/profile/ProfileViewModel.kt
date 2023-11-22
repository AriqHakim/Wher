package id.emergence.wher.screen.profile

import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.AuthRepository
import id.emergence.wher.domain.repository.ProfileRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepo: AuthRepository,
    private val profileRepo: ProfileRepository,
) : BaseViewModel() {
    private val mutableUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val user = mutableUser.asStateFlow()

    init {
        fetchProfile()
    }

    object LogoutSuccess : OneTimeEvent()

    private fun fetchProfile() {
        viewModelScope.launch {
            OneTimeEvent.Loading.send()
            profileRepo
                .fetchProfile()
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        mutableUser.emit(result.getOrNull())
                        OneTimeEvent.NotLoading.send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            authRepo.logout()
            LogoutSuccess.send()
        }
    }
}
