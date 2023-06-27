package com.soheibbettahar.litarus.di

import com.soheibbettahar.litarus.util.connectivity.ConnectivityManagerNetworkMonitor
import com.soheibbettahar.litarus.util.connectivity.NetworkMonitor
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