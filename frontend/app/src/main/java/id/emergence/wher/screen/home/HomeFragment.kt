package id.emergence.wher.screen.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentHomeBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModel<HomeViewModel>()

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = requestLocationPermissionLauncher { getCurrentLocation() }

    override fun onStart() {
        super.onStart()
        viewModel
            .events
            .onEach { event ->
                when (event) {
                    OneTimeEvent.Loading -> {
                        toggleLoading(true)
                    }

                    is OneTimeEvent.Error -> {
                        toggleLoading(false)
                        snackbar("Error : ${event.throwable?.message}")
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        with(binding) {
            val tabProfile = layoutInflater.inflate(R.layout.view_custom_tab, null)
            tabProfile.findViewById<ImageView>(R.id.icon).setBackgroundResource(R.drawable.ic_profile)
            val tabFriends = layoutInflater.inflate(R.layout.view_custom_tab, null)
            tabFriends.findViewById<ImageView>(R.id.icon).setBackgroundResource(R.drawable.ic_friends)

            tabLayout.addTab(tabLayout.newTab().setCustomView(tabProfile))
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabFriends))
            tabLayout.addOnTabSelectedListener(
                object : OnTabSelectedListener {
                    override fun onTabSelected(tab: Tab?) {
                        when (tab?.position) {
                            0 -> navigateTo(HomeFragmentDirections.actionHomeToProfile())
                            else -> navigateTo(HomeFragmentDirections.actionHomeToFriendList())
                        }
                    }

                    override fun onTabUnselected(tab: Tab?) {
                    }

                    override fun onTabReselected(tab: Tab?) {
                        when (tab?.position) {
                            0 -> navigateTo(HomeFragmentDirections.actionHomeToProfile())
                            else -> navigateTo(HomeFragmentDirections.actionHomeToFriendList())
                        }
                    }
                },
            )

            val mapView = childFragmentManager.findFragmentById(map.id) as SupportMapFragment
            lifecycle.coroutineScope.launch {
                mMap = mapView.awaitMap()
                setupMap()
            }
        }

        observeLocations()
    }

    private fun observeLocations() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.locations.collectLatest { list ->
                list.forEach { data ->
                    val latLng = LatLng(data.location.lat, data.location.lon)

                    mMap.addMarker {
                        position(latLng)
                        title(data.username)
                        snippet("#${data.id}")
                    }
                }
            }
        }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }

    private fun setupMap() {
        try {
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.custom_map_style,
                ),
            )
        } catch (e: Resources.NotFoundException) {
            logcat { "Can't find style. Error: $e" }
        }

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isRotateGesturesEnabled = true
        }

        getCurrentLocation()
    }

    // get current location
    private fun getCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLocation,
                            16f,
                        ),
                    )
                    mMap.addMarker {
                        position(currentLocation)
                        title("Lokasi kamu sekarang")
                    }
                }
            }
        } else {
            requestLocationPermission(locationPermissionLauncher)
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
