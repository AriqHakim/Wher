package id.emergence.wher.utils.viewbinding

import android.os.Handler
import android.os.Looper
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import logcat.logcat
import java.lang.reflect.Method

@MainThread
fun <T : ViewBinding> Fragment.viewBinding(bind: (View) -> T): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate.from(
        fragment = this,
        viewBindingBind = bind,
    )

@MainThread
inline fun <reified T : ViewBinding> Fragment.viewBinding(): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate.from(
        fragment = this,
        viewBindingClazz = T::class.java,
    )

@MainThread
inline fun <T : ViewBinding> ViewGroup.viewBinding(viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> T) =
    viewBindingFactory.invoke(LayoutInflater.from(this.context), this, false)

internal object MainHandler {
    private val HANDLER = Handler(Looper.getMainLooper())

    internal fun post(action: () -> Unit): Boolean = HANDLER.post(action)
}

@PublishedApi
internal fun ensureMainThread() =
    check(Looper.getMainLooper() == Looper.myLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }

internal object GetBindMethod {
    init {
        ensureMainThread()
    }

    private val METHOD_SIGNATURE = View::class.java
    private val METHOD_MAP = ArrayMap<Class<out ViewBinding>, Method>()

    internal operator fun <T : ViewBinding> invoke(clazz: Class<T>): Method =
        METHOD_MAP
            .getOrPut(clazz) { clazz.getMethod("bind", METHOD_SIGNATURE) }
            .also { logcat { "methodMap.size: ${METHOD_MAP.size}" } }
}
