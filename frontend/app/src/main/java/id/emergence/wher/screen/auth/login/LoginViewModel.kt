package id.emergence.wher.screen.auth.login

import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.model.LoginData
import id.emergence.wher.domain.repository.AuthRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: AuthRepository,
) : BaseViewModel() {
    private val mutableFormData = MutableStateFlow(LoginData())
    val formData = mutableFormData.asStateFlow()

    fun updateFormData(data: LoginData) {
        viewModelScope.launch {
            mutableFormData.emit(data)
        }
    }

    object LoginSuccess : OneTimeEvent()

    fun onLogin() {
        viewModelScope.launch {
            OneTimeEvent.Loading.send()
            auth
                .login(formData.value)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        LoginSuccess.send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }
}
