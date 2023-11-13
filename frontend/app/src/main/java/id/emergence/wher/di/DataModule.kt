package id.emergence.wher.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import id.emergence.wher.BuildConfig
import id.emergence.wher.data.prefs.DataStoreManager
import id.emergence.wher.data.remote.ApiService
import id.emergence.wher.data.remote.NoConnectionInterceptor
import kotlinx.serialization.json.Json
import logcat.logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val dataModule =
    module {
        val defaultTimeout = 2L
        val baseUrl = BuildConfig.BASE_URL

        val json =
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }

        single {
            NoConnectionInterceptor(androidContext())
        }

        single {
            val builder =
                OkHttpClient
                    .Builder()
                    .connectTimeout(defaultTimeout, TimeUnit.MINUTES)
                    .readTimeout(defaultTimeout, TimeUnit.MINUTES)
                    .writeTimeout(defaultTimeout, TimeUnit.MINUTES)

            // logging
            if (BuildConfig.DEBUG) {
                logcat { "Applying HTTP Logging" }
                val logger =
                    HttpLoggingInterceptor { message ->
                        logcat("API") { message }
                    }.apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                builder.addInterceptor(logger)
            }

            // detect internet connection
            builder.addInterceptor(get<NoConnectionInterceptor>())
            builder.build()
        }

        single {
            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .client(get())
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()
            retrofit.create(ApiService::class.java)
        }

        single {
            DataStoreManager(androidContext())
        }
    }
