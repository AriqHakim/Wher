package id.emergence.wher.utils.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val mutableEvents = Channel<OneTimeEvent>()
    val events: Flow<OneTimeEvent> = mutableEvents.receiveAsFlow()

    protected fun OneTimeEvent.send() {
        viewModelScope.launch {
            mutableEvents.send(this@send)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
