package id.emergence.wher.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.repository.AuthRepository
import id.emergence.wher.domain.repository.ProfileRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val auth: AuthRepository,
    private val profile : ProfileRepository
) : BaseViewModel() {
    object LogoutSuccess : OneTimeEvent()
    fun onLogout() {
        viewModelScope.launch {
            auth.logout()
            LogoutSuccess.send()
        }
    }
}
