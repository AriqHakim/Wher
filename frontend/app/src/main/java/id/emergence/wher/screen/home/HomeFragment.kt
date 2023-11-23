package id.emergence.wher.screen.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import id.emergence.wher.R
import id.emergence.wher.data.worker.LocationSharingWorker
import id.emergence.wher.databinding.FragmentHomeBinding
import id.emergence.wher.domain.model.FriendLocation
import id.emergence.wher.ext.dp
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.ext.toast
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import id.emergence.wher.domain.model.Location as ModelLocation

class HomeFragment :
    Fragment(
        id
            .emergence
            .wher
            .R
            .layout
            .fragment_home,
    ) {
    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModel<HomeViewModel>()
    private val imageLoader by inject<ImageLoader>()

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = requestLocationPermissionLauncher { showCurrentLocation() }

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

            fabAction.setOnClickListener {
                viewModel.onToggleLocation()
            }

            fabRefresh.setOnClickListener {
                try {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            viewModel.forceUpdateLocation(ModelLocation(lat = location.latitude, lon = location.longitude))
                        }
                    }
                } catch (ex: SecurityException) {
                    toast("Missing location permission")
                }
            }

            fabDetect.setOnClickListener {
                showCurrentLocation()
            }
        }

        observeLocations()
        observeIsSharingState()
        observeSharingTime()
    }

    private fun observeLocations() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.locations.collectLatest { list ->
                list.forEach { data ->
                    val latLng = LatLng(data.location.lat, data.location.lon)

                    mMap.addMarker {
                        position(latLng)
                        icon(BitmapDescriptorFactory.fromBitmap(drawMarker(data)))
                        title("@${data.username}")
                    }
                }
            }
        }

    private suspend fun drawMarker(data: FriendLocation): Bitmap {
        var bitmap: Bitmap? = null
        val request =
            ImageRequest
                .Builder(requireContext())
                .data(data.imgUrl)
                .allowHardware(false)
                .size(57)
                .transformations(CircleCropTransformation())
                .target(
                    onSuccess = { result ->
                        bitmap = result.toBitmapOrNull()
                    },
                ).build()
        imageLoader.execute(request)

        val bmp = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888)
        bmp.eraseColor(Color.TRANSPARENT)
        val canvas = Canvas(bmp)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.marker_pin)
        drawable?.setBounds(0, 0, dp(62), dp(76))
        drawable?.draw(canvas)

        val roundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val bitmapRect = RectF()
        canvas.save()

        bitmap?.let {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val matrix = Matrix()
            val scale = dp(52f) / (it.width.toFloat())
            matrix.postScale(scale, scale)
            matrix.postTranslate(dp(5f), dp(5f))
            roundPaint.shader = shader
            shader.setLocalMatrix(matrix)
            bitmapRect.set(dp(5f), dp(5f), dp(57f), dp(57f))
            canvas.drawRoundRect(bitmapRect, dp(26f), dp(26f), roundPaint)
        }
        return bmp
    }

    private fun observeIsSharingState() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSharing.distinctUntilChanged().collectLatest { isSharing ->
                if (isSharing) startSharing() else stopSharing()
                with(binding) {
                    fabAction.setImageResource(
                        if (isSharing) {
                            R.drawable.ic_pause
                        } else {
                            R.drawable.ic_play
                        },
                    )
                    fabAction.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireContext(),
                                if (isSharing) {
                                    R.color.brand_green
                                } else {
                                    R.color.brand_red
                                },
                            ),
                        )
                }
            }
        }

    private fun observeSharingTime() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.lastSharingTime.collectLatest {
                if (it.isNotEmpty()) {
                    val date = LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                    val newStr = date.format(DateTimeFormatter.ofPattern("HH:mm"))
                    binding.chipLastUpdate.text = "Last updated : $newStr"
                } else {
                    binding.chipLastUpdate.text = "Last updated : null"
                }
                viewModel.fetchLocations()
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

        showCurrentLocation()
    }

    // get current location
    private fun showCurrentLocation() {
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
                        title("Your location")
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

    // worker related
    private fun startSharing() {
        logcat { "startSharing()" }
        // init work
        val request =
            OneTimeWorkRequestBuilder<LocationSharingWorker>()
                .build()
        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueueUniqueWork(LocationSharingWorker.TAG, ExistingWorkPolicy.KEEP, request)
    }

    private fun stopSharing() {
        logcat { "stopSharing()" }
        val workManager = WorkManager.getInstance(requireContext())
        workManager.cancelUniqueWork(LocationSharingWorker.TAG)
    }
}
