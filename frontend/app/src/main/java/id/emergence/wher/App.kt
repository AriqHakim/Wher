package id.emergence.wher

import android.app.Application
import id.emergence.wher.di.dataModule
import id.emergence.wher.di.domainModule
import id.emergence.wher.di.uiModule
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)

        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            // modules
            modules(
                listOf(
                    dataModule,
                    domainModule,
                    uiModule,
                ),
            )
        }
    }
}
