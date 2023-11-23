package id.emergence.wher.ext

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import logcat.logcat
import kotlin.math.ceil

private fun Fragment.createSnackbar(
    message: String,
    duration: Int,
): Snackbar = Snackbar.make(requireView(), message, duration)

fun Fragment.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    createSnackbar(message, duration).show()
}

fun Fragment.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.navigateTo(directions: NavDirections) = findNavController().navigate(directions)

fun Fragment.hideKeyboard() {
    try {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    } catch (e: Exception) {
        logcat { e.message.toString() }
    }
}

fun Fragment.dp(value: Float): Float = ceil(resources.displayMetrics.density * value)

fun Fragment.dp(value: Int): Int = ceil(resources.displayMetrics.density * value).toInt()
