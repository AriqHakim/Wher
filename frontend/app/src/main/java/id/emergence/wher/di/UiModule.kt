package id.emergence.wher.di

import coil.ImageLoader
import id.emergence.wher.data.worker.LocationSharingWorker
import id.emergence.wher.screen.auth.login.LoginViewModel
import id.emergence.wher.screen.auth.register.RegisterViewModel
import id.emergence.wher.screen.friends.detail.FriendDetailViewModel
import id.emergence.wher.screen.friends.list.FriendListViewModel
import id.emergence.wher.screen.friends.request.FriendRequestViewModel
import id.emergence.wher.screen.friends.search.FriendSearchViewModel
import id.emergence.wher.screen.home.HomeViewModel
import id.emergence.wher.screen.profile.ProfileViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val uiModule =
    module {
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::FriendDetailViewModel)
        viewModelOf(::FriendRequestViewModel)
        viewModelOf(::FriendListViewModel)
        viewModelOf(::FriendSearchViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::HomeViewModel)

        single {
            ImageLoader
                .Builder(androidContext())
                .okHttpClient(get<OkHttpClient>())
                .crossfade(true)
                .build()
        }

        worker {
            LocationSharingWorker(appContext = androidContext(), params = get(), repo = get())
        }
    }
