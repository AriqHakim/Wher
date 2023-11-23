package id.emergence.wher.data.worker

import android.content.Context
import android.location.Location
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import id.emergence.wher.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import logcat.logcat
import java.time.Duration
import id.emergence.wher.domain.model.Location as ModelLocation

class LocationSharingWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repo: LocationRepository,
) : CoroutineWorker(appContext, params) {
    companion object {
        const val TAG = "location_sharing_worker"
        const val KEY_MESSAGE = "message"

        fun scheduleNextWork(context: Context) {
            val constraints =
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            val request =
                OneTimeWorkRequestBuilder<LocationSharingWorker>()
                    .setConstraints(constraints)
                    .setInitialDelay(Duration.ofMinutes(15))
                    .build()
            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, request)
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override suspend fun doWork(): Result {
        logcat { "doWork() - sharing location" }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        return try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = repo.updateLocation(ModelLocation(location.latitude, location.longitude)).first()
                        if (result.isSuccess) {
                            this.cancel()
                        } else {
                            throw IllegalStateException("update location failed")
                        }
                    }
                }
            }
            scheduleNextWork(applicationContext)
            Result.success(workDataOf(KEY_MESSAGE to ""))
        } catch (ex: SecurityException) {
            Result.failure(
                workDataOf(KEY_MESSAGE to ex.message),
            )
        } catch (ex: IllegalStateException) {
            Result.failure(
                workDataOf(KEY_MESSAGE to ex.message),
            )
        }
    }
}
