package id.emergence.wher.di

import id.emergence.wher.data.AuthRepositoryImpl
import id.emergence.wher.data.FriendRepositoryImpl
import id.emergence.wher.data.LocationRepositoryImpl
import id.emergence.wher.data.ProfileRepositoryImpl
import id.emergence.wher.domain.repository.AuthRepository
import id.emergence.wher.domain.repository.FriendRepository
import id.emergence.wher.domain.repository.LocationRepository
import id.emergence.wher.domain.repository.ProfileRepository
import org.koin.dsl.module

val domainModule =
    module {
        single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        single<FriendRepository> { FriendRepositoryImpl(get()) }
        single<LocationRepository> { LocationRepositoryImpl(get()) }
        single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    }
