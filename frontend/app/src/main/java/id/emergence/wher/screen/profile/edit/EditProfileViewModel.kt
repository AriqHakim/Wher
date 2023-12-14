package id.emergence.wher.screen.profile.edit

import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.model.ProfileData
import id.emergence.wher.domain.model.User
import id.emergence.wher.domain.repository.ProfileRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repo: ProfileRepository,
) : BaseViewModel() {
    private val mutableUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val user = mutableUser.asStateFlow()

    private val mutableFormData = MutableStateFlow(ProfileData())
    val formData = mutableFormData.asStateFlow()

    fun updateFormData(data: ProfileData) {
        viewModelScope.launch {
            mutableFormData.emit(data)
        }
    }

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            OneTimeEvent.Loading.send()
            repo
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

    object UpdateProfileSucceed : OneTimeEvent()

    fun updateProfile() {
        viewModelScope.launch {
            OneTimeEvent.Loading.send()
            repo
                .updateProfile(formData.value)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        UpdateProfileSucceed.send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }
}
