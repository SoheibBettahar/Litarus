package com.example.guttenburg.di

import com.example.guttenburg.util.ConnectivityManagerNetworkMonitor
import com.example.guttenburg.util.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkMonitorModule {

    @Binds
    abstract fun bindNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor
}