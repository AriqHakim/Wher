package id.emergence.wher.screen.auth.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentSplashBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.utils.viewbinding.viewBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val binding by viewBinding<FragmentSplashBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val logoSpannable = SpannableString("Wher?")
            logoSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_green2)),
                4,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            lblLogo.text = logoSpannable
            val firstTimeSpannable = SpannableString("First Time Using Wher?")
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_green2)),
                21,
                22,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_blue)),
                17,
                21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            lblFirstTime.text = firstTimeSpannable
            lblSignUp.paintFlags = lblSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            btnSignIn.setOnClickListener {
                navigateTo(SplashFragmentDirections.actionSplashToLogin())
            }
            lblSignUp.setOnClickListener {
                navigateTo(SplashFragmentDirections.actionSplashToRegister())
            }
        }
    }

    // location shenanigans
    private fun Fragment.checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            permission,
        ) == PackageManager.PERMISSION_GRANTED

    // location permission shenanigans
    private fun requestLocationPermission(launcher: ActivityResultLauncher<Array<String>>) {
        MaterialAlertDialogBuilder(requireContext())
            .apply {
                setTitle("Requesting Location Permission")
                setMessage("This app requires location permission to share your location to your friends")
                setPositiveButton("OK") { _, _ ->
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        ),
                    )
                }
                setNegativeButton("Cancel") { _, _ ->
                    // do nothing
                }
            }.show()
    }

    private fun onPermissionDenied() {
        MaterialAlertDialogBuilder(requireContext())
            .apply {
                setTitle("Location Permission Denied")
                setMessage(
                    "Without permission, this app can't function correctly. Please give the location permission to ensure the app is running as intended.",
                )
                setPositiveButton("OK") { _, _ ->
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                        it.data = Uri.fromParts("package", requireActivity().packageName, null)
                        startActivity(it)
                    }
                }
            }.show()
    }

    private fun requestLocationPermissionLauncher(onPermissionAccepted: () -> Unit): ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    onPermissionAccepted.invoke()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    onPermissionAccepted.invoke()
                }

                else -> {
                    onPermissionDenied()
                }
            }
        }
}
