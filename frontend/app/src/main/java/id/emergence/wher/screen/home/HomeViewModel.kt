package id.emergence.wher.screen.home

import androidx.lifecycle.viewModelScope
import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.domain.model.Location
import id.emergence.wher.domain.repository.LocationRepository
import id.emergence.wher.utils.base.BaseViewModel
import id.emergence.wher.utils.base.OneTimeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import logcat.logcat

class HomeViewModel(
    private val repo: LocationRepository,
) : BaseViewModel() {
    private val page = MutableStateFlow(1)
    private val isEndPagination = MutableStateFlow(false)

    private val mLocations = MutableStateFlow<List<FriendLocation>>(emptyList())
    val locations = mLocations.asStateFlow()

    val lastSharingTime = repo.getLastSharingTime()
    val isSharing = repo.checkIsSharingLocation()

    init {
        fetchLocations()
    }

    fun fetchLocations() {
        viewModelScope.launch {
            val currentLocations = mLocations.value
            repo
                .fetchLocations(page.value)
                .catch { OneTimeEvent.Error(it).send() }
                .collectLatest { result ->
                    if (result.isSuccess) {
                        val data = result.getOrThrow()
                        if (data.isEmpty()) {
                            isEndPagination.emit(true)
                        }
                        mLocations.emit((currentLocations + data).distinct())
                    } else if (result.isFailure) {
                        OneTimeEvent.Error(result.exceptionOrNull()).send()
                    }
                }
            logcat { "mapsData -> page = ${page.value} and totalItems = ${mLocations.value.size}" }
            page.emit(page.value.plus(1))
        }
    }

    fun onToggleLocation() {
        viewModelScope.launch {
            repo.toggleSharingLocation(!isSharing.first())
        }
    }

    fun forceUpdateLocation(location: Location) {
        viewModelScope.launch {
            repo.updateLocation(location)
        }
    }
}
