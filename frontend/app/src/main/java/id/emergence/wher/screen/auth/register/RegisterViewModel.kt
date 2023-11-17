package id.emergence.wher.screen.auth.register

import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.model.RegisterData
import id.emergence.wher.domain.repository.AuthRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val auth: AuthRepository,
) : BaseViewModel() {
    private val mutableFormData = MutableStateFlow(RegisterData())
    val formData = mutableFormData.asStateFlow()

    fun updateFormData(data: RegisterData) {
        viewModelScope.launch {
            mutableFormData.emit(data)
        }
    }

    object RegisterSuccess : OneTimeEvent()

    fun onRegister() {
        viewModelScope.launch {
            OneTimeEvent.Loading.send()
            auth
                .register(formData.value)
                .catch { OneTimeEvent.Error(it).send() }
                .collect { result ->
                    if (result.isSuccess) {
                        RegisterSuccess.send()
                    } else {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
        }
    }
}
