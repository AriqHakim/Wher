package id.emergence.wher.utils.base

open class OneTimeEvent {
    object Loading : OneTimeEvent()

    data class Error(
        val throwable: Throwable?,
    ) : OneTimeEvent()
}
