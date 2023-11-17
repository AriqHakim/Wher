package id.emergence.wher.utils.base

open class OneTimeEvent {
    object Loading : OneTimeEvent()

    object NotLoading : OneTimeEvent()

    data class Alert(
        val message: String?,
    ) : OneTimeEvent()

    data class Error(
        val throwable: Throwable?,
    ) : OneTimeEvent()
}
